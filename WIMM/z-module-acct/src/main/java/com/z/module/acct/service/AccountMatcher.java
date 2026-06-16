package com.z.module.acct.service;

import com.z.module.acct.domain.AccountCls;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 账户识别器: 在单表结构下程序区分"账户"与"费用科目".
 * <p>
 * 背景: acct_account_cls 同时承载"会计科目"(餐饮/工资, 仅分类)和"账户"
 * (现金/微信/信用卡, 能实际收付款、持有余额). 两者平铺在一张表,
 * 由本类按规则判定, 为 AI 录入("微信付的")定位收付款账户提供依据.
 * <p>
 * 判定规则: 一级为资产(001)或负债(002), 且为末级科目.
 * 收入(003)/支出(004)类永远不是账户.
 *
 * @author zhaozhiwei
 */
@Component
public class AccountMatcher {

    /** 一级科目 code 前缀: 资产 */
    private static final String PREFIX_ASSET = "001";
    /** 一级科目 code 前缀: 负债 */
    private static final String PREFIX_LIABILITY = "002";

    /**
     * 账户分类: 用于 AI 录入时把"微信/支付宝/现金/信用卡"等自然语言映射到具体账户.
     * key = 科目 code, value = 账户语义标签(便于扩展多语言/别名匹配).
     */
    private static final Map<String, String> ACCOUNT_LABEL_BY_CODE = Map.ofEntries(
            Map.entry("001001", "cash"),        // 现金
            Map.entry("001002", "bank"),        // 银行存款
            Map.entry("001003", "wechat"),      // 微信
            Map.entry("001004", "alipay"),      // 支付宝
            Map.entry("001005", "huabei"),      // 花呗
            Map.entry("001006", "investment"),  // 投资
            Map.entry("001007", "fixed_asset"), // 固定资产
            Map.entry("001008", "other_asset"), // 其他资产
            Map.entry("002001", "loan"),        // 贷款
            Map.entry("002002", "credit_card"), // 信用卡
            Map.entry("002003", "mortgage"),    // 抵押
            Map.entry("002004", "other_debt")   // 其他负债
    );

    /**
     * 判断某科目是否为"账户"(可收付款/持有余额).
     */
    public boolean isAccount(AccountCls accountCls) {
        if (accountCls == null || accountCls.getCode() == null) {
            return false;
        }
        // 非末级(如一级资产/负债本身)不算账户
        if (accountCls.getIsLeaf() != null && accountCls.getIsLeaf() == 0) {
            return false;
        }
        return isAccountCode(accountCls.getCode());
    }

    /**
     * 判断某科目 code 是否属于账户类(资产/负债下的末级).
     */
    public boolean isAccountCode(String code) {
        if (code == null) {
            return false;
        }
        return code.startsWith(PREFIX_ASSET) || code.startsWith(PREFIX_LIABILITY);
    }

    /**
     * 取账户语义标签(供 AI 别名匹配). 非账户返回空.
     */
    public Optional<String> accountLabel(String code) {
        if (!isAccountCode(code)) {
            return Optional.empty();
        }
        return Optional.ofNullable(ACCOUNT_LABEL_BY_CODE.get(code));
    }
}
