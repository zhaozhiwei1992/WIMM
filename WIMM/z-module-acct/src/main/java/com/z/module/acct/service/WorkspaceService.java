package com.z.module.acct.service;

import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.repository.VoucherDetailRepository;
import com.z.module.acct.web.vo.AssetVO;
import com.z.module.acct.web.vo.LineDataVO;
import com.z.module.acct.web.vo.PieDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作台 Service
 */
@Service
@Slf4j
public class WorkspaceService {

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
        List<AccountCls> all = accountClsRepository.findAll();
        Map<Long, List<AccountCls>> collect = all.stream()
                .collect(Collectors.groupingBy(AccountCls::getParentId));

        // 资产
        List<String> assetCodes = collect.getOrDefault(1L, Collections.emptyList())
                .stream().map(AccountCls::getCode).toList();
        BigDecimal totalAssets = sumAmountsByCodes(assetCodes);
        assetVO.setTotalAssets(totalAssets);

        // 负债
        List<String> liabilityCodes = collect.getOrDefault(2L, Collections.emptyList())
                .stream().map(AccountCls::getCode).toList();
        BigDecimal totalLiability = sumAmountsByCodes(liabilityCodes);
        assetVO.setTotalLiabilities(totalLiability);

        // 净资产
        assetVO.setNetWorth(totalAssets.subtract(totalLiability));
        return assetVO;
    }

    /**
     * 月度收支趋势
     */
    public List<LineDataVO> getMonthlyIncExpData() {
        List<AccountCls> all = accountClsRepository.findAll();
        Map<Long, List<AccountCls>> collect = all.stream()
                .collect(Collectors.groupingBy(AccountCls::getParentId));

        // 收入
        List<String> incCodes = collect.getOrDefault(3L, Collections.emptyList())
                .stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> incList = voucherDetailRepository.findAllByAcctClsCodeIn(incCodes);

        // 支出
        List<String> expCodes = collect.getOrDefault(4L, Collections.emptyList())
                .stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> expList = voucherDetailRepository.findAllByAcctClsCodeIn(expCodes);

        // 按月分组
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
        return getPieDataByParentId(3L);
    }

    /**
     * 支出饼图数据
     */
    public List<PieDataVO> getExpPieData() {
        return getPieDataByParentId(4L);
    }

    // ========== 私有方法 ==========

    /**
     * 按科目代码汇总金额，区分借贷方向
     * 借方分录取正值，贷方分录取负值
     */
    private BigDecimal sumAmountsByCodes(List<String> codes) {
        if (codes.isEmpty()) return BigDecimal.ZERO;
        return voucherDetailRepository.findAllByAcctClsCodeIn(codes)
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

    private List<PieDataVO> getPieDataByParentId(Long parentId) {
        List<AccountCls> accountClsList = accountClsRepository.findAllByParentIdOrderByOrderNumAsc(parentId);
        Map<String, String> nameMap = accountClsList.stream()
                .collect(Collectors.toMap(AccountCls::getCode, AccountCls::getName));
        List<String> codes = accountClsList.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> voucherDetails = voucherDetailRepository.findAllByAcctClsCodeIn(codes);

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
