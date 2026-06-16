package com.z.module.acct.service;

import com.z.framework.security.util.SecurityUtils;
import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.web.vo.AiParseRaw;
import com.z.module.acct.web.vo.AiParseResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * AI 记账解析服务.
 * <p>
 * 将用户自然语言(如"买衣服花了200, 微信付的")解析为复式记账所需的双科目结构,
 * 借贷方向由记账接口推导, 用户无需理解借贷.
 * <p>
 * 流程: 取本家庭科目作上下文 → 构造 prompt → 调 OpenAI → 校验科目 → 兜底.
 *
 * @author zhaozhiwei
 */
@Service
@Slf4j
public class AiBookkeepingService {

    // 兜底科目 code
    private static final String FALLBACK_EXPENSE = "004010"; // 其他费用
    private static final String FALLBACK_INCOME = "003003";  // 其他收入
    private static final String DEFAULT_ACCOUNT_CODE = "001001"; // 现金(默认收付款账户)

    private final ChatClient.Builder chatClientBuilder;
    private final AccountClsRepository accountClsRepository;
    private final AccountMatcher accountMatcher;

    public AiBookkeepingService(ChatClient.Builder chatClientBuilder,
                                AccountClsRepository accountClsRepository,
                                AccountMatcher accountMatcher) {
        this.chatClientBuilder = chatClientBuilder;
        this.accountClsRepository = accountClsRepository;
        this.accountMatcher = accountMatcher;
    }

    /**
     * 解析用户文本为记账预览结构.
     */
    public AiParseResultVO parse(String text) {
        String tenantId = SecurityUtils.getTenantId();
        List<AccountCls> accounts = accountClsRepository.findAllByTenantIdOrderByOrderNumAsc(tenantId);

        if (accounts.isEmpty()) {
            throw new RuntimeException("当前家庭未初始化科目, 无法解析");
        }

        // 1. 构造科目上下文(账户类 + 收支类末级), 喂给 AI
        String subjectContext = buildSubjectContext(accounts);

        // 2. 调 AI 解析
        AiParseRaw raw = callAi(text, subjectContext);

        // 3. 名称 → code 匹配 + 兜底
        return buildResult(raw, accounts);
    }

    /**
     * 构造喂给 AI 的科目清单: 区分"账户类"和"收支类", 用 code 名称格式.
     */
    private String buildSubjectContext(List<AccountCls> accounts) {
        StringBuilder sb = new StringBuilder();
        sb.append("可选账户(用于收付款/转账):\n");
        accounts.stream()
                .filter(accountMatcher::isAccount)
                .forEach(a -> sb.append("- ").append(a.getCode()).append(" ").append(a.getName()).append("\n"));
        sb.append("\n可选收支科目(用于记账分类):\n");
        accounts.stream()
                .filter(a -> !accountMatcher.isAccount(a) && isLeaf(a))
                .forEach(a -> sb.append("- ").append(a.getCode()).append(" ").append(a.getName()).append("\n"));
        return sb.toString();
    }

    private boolean isLeaf(AccountCls a) {
        return a.getIsLeaf() == null || a.getIsLeaf() == 1;
    }

    /**
     * 调用 OpenAI, 用 BeanOutputConverter 约束返回 AiParseRaw 的 JSON.
     */
    private AiParseRaw callAi(String text, String subjectContext) {
        String systemPrompt = """
                你是家庭记账助手。把用户的记账描述解析为结构化数据。
                必须使用给定科目清单中的【科目名称】, 不要编造。
                交易类型 type 只能是: income(收入) / expense(支出) / transfer(转账, 钱在账户间流动)。
                bizAccountName: 支出/收入的业务科目名称(如"衣服"/"工资"); transfer 时填源账户名称。
                payAccountName: 收付款账户名称(如"微信"/"现金"); transfer 时填目标账户名称。
                amt: 金额, 正数。remark: 简短备注。
                输出 JSON。

                当前家庭可用科目清单:
                """ + subjectContext;

        return chatClientBuilder.build()
                .prompt()
                .system(systemPrompt)
                .user(text)
                .call()
                .entity(AiParseRaw.class);
    }

    /**
     * 把 AI 返回的名称匹配到本家庭真实 code, 匹配不到走兜底, 并推导借贷方向.
     */
    private AiParseResultVO buildResult(AiParseRaw raw, List<AccountCls> accounts) {
        AiParseResultVO vo = new AiParseResultVO();
        boolean fallback = false;
        String type = raw.getType() == null ? "expense" : raw.getType().toLowerCase();
        vo.setType(type);

        BigDecimal amt = raw.getAmt() == null ? BigDecimal.ZERO : raw.getAmt();
        vo.setAmt(amt);
        vo.setRemark(raw.getRemark());

        // 按名称模糊匹配科目(忽略大小写/空白)
        AccountCls biz = matchByName(accounts, raw.getBizAccountName());
        AccountCls pay = matchByName(accounts, raw.getPayAccountName());

        switch (type) {
            case "income" -> {
                // 收入: 业务=收入科目, 账户=收款账户
                if (biz == null || accountMatcher.isAccount(biz)) {
                    biz = findByCode(accounts, FALLBACK_INCOME);
                    fallback = true;
                }
                if (pay == null || !accountMatcher.isAccount(pay)) {
                    pay = findByCode(accounts, DEFAULT_ACCOUNT_CODE);
                    if (pay == null) pay = firstAccount(accounts);
                    fallback = true;
                }
                // 借: 账户(资产增加), 贷: 收入
                setNames(vo, biz, pay);
                vo.setDebitAccount(pay.getCode());
                vo.setCreditAccount(biz.getCode());
            }
            case "transfer" -> {
                // 转账: 两个都是账户
                if (pay == null || !accountMatcher.isAccount(pay)) {
                    pay = findByCode(accounts, DEFAULT_ACCOUNT_CODE);
                    if (pay == null) pay = firstAccount(accounts);
                    fallback = true;
                }
                if (biz == null || !accountMatcher.isAccount(biz)) {
                    biz = firstAccount(accounts);
                    fallback = true;
                }
                setNames(vo, biz, pay);
                // 借: 目标账户(pay, 资产增加), 贷: 源账户(biz, 资产减少)
                vo.setDebitAccount(pay.getCode());
                vo.setCreditAccount(biz.getCode());
            }
            default -> {
                // 支出: 业务=支出科目, 账户=付款账户
                if (biz == null || accountMatcher.isAccount(biz)) {
                    biz = findByCode(accounts, FALLBACK_EXPENSE);
                    fallback = true;
                }
                if (pay == null || !accountMatcher.isAccount(pay)) {
                    pay = findByCode(accounts, DEFAULT_ACCOUNT_CODE);
                    if (pay == null) pay = firstAccount(accounts);
                    fallback = true;
                }
                setNames(vo, biz, pay);
                // 借: 支出, 贷: 账户(资产减少)
                vo.setDebitAccount(biz.getCode());
                vo.setCreditAccount(pay.getCode());
            }
        }
        vo.setFallback(fallback);
        return vo;
    }

    private void setNames(AiParseResultVO vo, AccountCls biz, AccountCls pay) {
        vo.setBizAccountCode(biz.getCode());
        vo.setBizAccountName(biz.getName());
        vo.setPayAccountCode(pay.getCode());
        vo.setPayAccountName(pay.getName());
    }

    /**
     * 名称模糊匹配: 忽略大小写与首尾空白, 包含关系优先.
     */
    private AccountCls matchByName(List<AccountCls> accounts, String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String key = name.trim().toLowerCase();
        return accounts.stream()
                .filter(a -> a.getName() != null && a.getName().trim().equalsIgnoreCase(key))
                .findFirst()
                .orElseGet(() -> accounts.stream()
                        .filter(a -> a.getName() != null && a.getName().toLowerCase().contains(key))
                        .findFirst()
                        .orElse(null));
    }

    private AccountCls findByCode(List<AccountCls> accounts, String code) {
        return accounts.stream()
                .filter(a -> Objects.equals(a.getCode(), code))
                .findFirst()
                .orElse(null);
    }

    private AccountCls firstAccount(List<AccountCls> accounts) {
        return accounts.stream().filter(accountMatcher::isAccount).findFirst().orElse(null);
    }
}
