package com.z.module.acct.service;

import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.repository.VoucherDetailRepository;
import com.z.module.acct.web.vo.AssetVO;
import com.z.module.acct.web.vo.LineDataVO;
import com.z.module.acct.web.vo.PieDataVO;
import com.z.framework.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作台 Service
 * <p>
 * 多租户隔离: 所有科目/凭证查询均限定当前家庭的 tenant_id.
 * 一级科目通过 code(001资产/002负债/003收入/004支出) 定位, 而非固定 id,
 * 因为每个家庭有独立的科目副本, id 各不相同.
 */
@Service
@Slf4j
public class WorkspaceService {

    private static final String CODE_ASSET = "001";      // 资产
    private static final String CODE_LIABILITY = "002";  // 负债
    private static final String CODE_INCOME = "003";     // 收入
    private static final String CODE_EXPENSE = "004";    // 支出

    private final VoucherDetailRepository voucherDetailRepository;
    private final AccountClsRepository accountClsRepository;

    public WorkspaceService(VoucherDetailRepository voucherDetailRepository,
                            AccountClsRepository accountClsRepository) {
        this.voucherDetailRepository = voucherDetailRepository;
        this.accountClsRepository = accountClsRepository;
    }

    /**
     * 获取资产概况
     */
    public AssetVO getAsset() {
        AssetVO assetVO = new AssetVO();

        // 资产 = 一级'资产'(code=001) 下所有二级科目余额之和
        List<String> assetCodes = childCodesOf(CODE_ASSET);
        BigDecimal totalAssets = sumAmountsByCodes(assetCodes);
        assetVO.setTotalAssets(totalAssets);

        // 负债 = 一级'负债'(code=002) 下所有二级科目余额之和
        List<String> liabilityCodes = childCodesOf(CODE_LIABILITY);
        BigDecimal totalLiability = sumAmountsByCodes(liabilityCodes);
        assetVO.setTotalLiabilities(totalLiability);

        // 净资产 = 资产 - 负债
        assetVO.setNetWorth(totalAssets.subtract(totalLiability));
        return assetVO;
    }

    /**
     * 月度收支趋势
     */
    public List<LineDataVO> getMonthlyIncExpData() {
        String tenantId = SecurityUtils.getTenantId();

        List<String> incCodes = childCodesOf(CODE_INCOME);
        List<VoucherDetail> incList = voucherDetailRepository.findAllByTenantIdAndAcctClsCodeIn(tenantId, incCodes);

        List<String> expCodes = childCodesOf(CODE_EXPENSE);
        List<VoucherDetail> expList = voucherDetailRepository.findAllByTenantIdAndAcctClsCodeIn(tenantId, expCodes);

        Map<String, List<VoucherDetail>> incMap = groupByMonth(incList);
        Map<String, List<VoucherDetail>> expMap = groupByMonth(expList);

        List<LineDataVO> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String month = i < 10 ? "0" + i : String.valueOf(i);
            LineDataVO vo = new LineDataVO();
            vo.setName(month);

            BigDecimal incTotal = incMap.getOrDefault(month, Collections.emptyList())
                    .stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setLine1(incTotal);

            BigDecimal expTotal = expMap.getOrDefault(month, Collections.emptyList())
                    .stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setLine2(expTotal);

            result.add(vo);
        }
        return result;
    }

    /**
     * 收入饼图数据
     */
    public List<PieDataVO> getIncPieData() {
        return getPieDataByParentCode(CODE_INCOME);
    }

    /**
     * 支出饼图数据
     */
    public List<PieDataVO> getExpPieData() {
        return getPieDataByParentCode(CODE_EXPENSE);
    }

    // ========== 私有方法 ==========

    /**
     * 取某个一级科目(code)下所有二级科目的 code 列表(当前家庭)
     */
    private List<String> childCodesOf(String parentCode) {
        String tenantId = SecurityUtils.getTenantId();
        AccountCls parent = accountClsRepository
                .findAllByTenantIdAndCodeIn(tenantId, List.of(parentCode))
                .stream().findFirst().orElse(null);
        if (parent == null) {
            return Collections.emptyList();
        }
        return accountClsRepository
                .findAllByTenantIdAndParentIdOrderByOrderNumAsc(tenantId, parent.getId())
                .stream().map(AccountCls::getCode).toList();
    }

    /**
     * 按科目代码汇总金额，区分借贷方向
     * 借方分录取正值，贷方分录取负值
     */
    private BigDecimal sumAmountsByCodes(List<String> codes) {
        if (codes.isEmpty()) return BigDecimal.ZERO;
        return voucherDetailRepository
                .findAllByTenantIdAndAcctClsCodeIn(SecurityUtils.getTenantId(), codes)
                .stream()
                .map(v -> v.getDrCr() == 1 ? v.getAmt() : v.getAmt().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, List<VoucherDetail>> groupByMonth(List<VoucherDetail> list) {
        return list.stream().collect(Collectors.groupingBy(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MM");
            return sdf.format(Date.from(v.getCreatedDate()));
        }));
    }

    private List<PieDataVO> getPieDataByParentCode(String parentCode) {
        String tenantId = SecurityUtils.getTenantId();
        List<String> codes = childCodesOf(parentCode);
        if (codes.isEmpty()) {
            return Collections.emptyList();
        }

        // 科目 code -> 名称
        Map<String, String> nameMap = accountClsRepository
                .findAllByTenantIdAndCodeIn(tenantId, codes)
                .stream().collect(Collectors.toMap(AccountCls::getCode, AccountCls::getName));

        List<VoucherDetail> voucherDetails = voucherDetailRepository
                .findAllByTenantIdAndAcctClsCodeIn(tenantId, codes);

        Map<String, List<VoucherDetail>> grouped = voucherDetails.stream()
                .collect(Collectors.groupingBy(VoucherDetail::getAcctClsCode));

        return grouped.entrySet().stream().map(entry -> {
            PieDataVO vo = new PieDataVO();
            vo.setName(nameMap.get(entry.getKey()));
            vo.setValue(entry.getValue().stream().map(VoucherDetail::getAmt)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            return vo;
        }).toList();
    }
}
