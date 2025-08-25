package com.z.module.acct.web.rest;

import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.repository.VoucherDetailRepository;
import com.z.module.acct.web.vo.AssetVO;
import com.z.module.acct.web.vo.LineDataVO;
import com.z.module.acct.web.vo.PieDataVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: WorkspaceResource
 * @Package com/z/module/system/web/rest/WorkspaceResource.java
 * @Description: 工作台信息加载
 * @date 2024/10/12 17:01
 */

@Tag(name = "工作台API")
@RestController
@RequestMapping(value = "/acct/workplace")
@Slf4j
public class AcctWorkspaceResource {

    private final VoucherDetailRepository voucherDetailRepository;
    private final AccountClsRepository accountClsRepository;

    public AcctWorkspaceResource(VoucherDetailRepository voucherDetailRepository, AccountClsRepository accountClsRepository) {
        this.voucherDetailRepository = voucherDetailRepository;
        this.accountClsRepository = accountClsRepository;
    }

    @GetMapping("asset")
    public AssetVO asset() {
        AssetVO assetVO = new AssetVO();
        List<AccountCls> all = accountClsRepository.findAll();
        Map<Long, List<AccountCls>> collect = all.stream().collect(Collectors.groupingBy(AccountCls::getParentId));
        // 通过分录入表获取资产总数
        // 获取所有资产科目
        List<AccountCls> assetAccountClsList = collect.get(1L);
        List<String> list = assetAccountClsList.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> allAssetByAcctClsCodeIn = voucherDetailRepository.findAllByAcctClsCodeIn(list);
        BigDecimal totalAssets = allAssetByAcctClsCodeIn.stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
        assetVO.setTotalAssets(totalAssets);
        // 获取负债总数
        List<AccountCls> liabilitiyAccountClsList = collect.get(2L);
        List<String> list1 = liabilitiyAccountClsList.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> allLiabilitiyByAcctClsCodeIn1 = voucherDetailRepository.findAllByAcctClsCodeIn(list1);
        BigDecimal totalLiabilitiy = allLiabilitiyByAcctClsCodeIn1.stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
        assetVO.setTotalLiabilities(totalLiabilitiy);

        // 计算净资产
        assetVO.setNetWorth(totalAssets.subtract(totalLiabilitiy));
        return assetVO;
    }

    @GetMapping("monthlyIncExpData")
    public List<LineDataVO> incExpDataLine() {
        // 获取收入支出信息,并且按月统计
        List<AccountCls> all = accountClsRepository.findAll();
        Map<Long, List<AccountCls>> collect = all.stream().collect(Collectors.groupingBy(AccountCls::getParentId));
        // 收入
        List<AccountCls> incAccountClsList = collect.get(3L);
        List<String> incAccountStrList = incAccountClsList.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> incVoucherDetailList = voucherDetailRepository.findAllByAcctClsCodeIn(incAccountStrList);

        // 支出
        List<AccountCls> expAccountClsList = collect.get(4L);
        List<String> expAccountStrList = expAccountClsList.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> expVoucherDetailList = voucherDetailRepository.findAllByAcctClsCodeIn(expAccountStrList);

        // 根据创建日期, 按月分组统计
        Map<String, List<VoucherDetail>> incMap = incVoucherDetailList.stream().collect(Collectors.groupingBy(voucherDetail -> {
                    Date createDate = Date.from(voucherDetail.getCreatedDate());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                    return simpleDateFormat.format(createDate);
                }
        ));
        Map<String, List<VoucherDetail>> expMap = expVoucherDetailList.stream().collect(Collectors.groupingBy(voucherDetail -> {
                    Date createDate = Date.from(voucherDetail.getCreatedDate());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                    return simpleDateFormat.format(createDate);
                }
        ));

        List<LineDataVO> lineDataVOList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            String month = i < 10 ? "0" + i : String.valueOf(i);
            LineDataVO lineDataVO = new LineDataVO();
            lineDataVO.setName(month);
            // 收入
            List<VoucherDetail> voucherDetails = incMap.get(month);
            BigDecimal incTotal = voucherDetails == null ? BigDecimal.ZERO : voucherDetails.stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            lineDataVO.setLine1(incTotal);

            // 支出
            List<VoucherDetail> expVoucherDetails = expMap.get(month);
            BigDecimal expTotal = expVoucherDetails == null ? BigDecimal.ZERO : expVoucherDetails.stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            lineDataVO.setLine2(expTotal);
            lineDataVOList.add(lineDataVO);
        }

        return lineDataVOList;
    }

    /**
     * @Description: 统计每个科目下收入情况
     * @author: zhaozhiwei
     * @data: 2024/10/31-10:30
     * @return: java.util.List<com.z.module.acct.web.vo.PieDataVO>
     */

    @GetMapping("incPieData")
    public List<PieDataVO> incDataPie() {
        // 获取所有收入科目
        List<AccountCls> all = accountClsRepository.findAllByParentIdOrderByOrderNumAsc(3L);
        Map<String, String> map = all.stream().collect(Collectors.toMap(AccountCls::getCode, AccountCls::getName));
        List<String> list = all.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> allByAcctClsCodeIn = voucherDetailRepository.findAllByAcctClsCodeIn(list);
        // 按照科目分组统计每个科目下合计, 并写入到PieData
        Map<String, List<VoucherDetail>> collect = allByAcctClsCodeIn.stream().collect(Collectors.groupingBy(VoucherDetail::getAcctClsCode));
        return collect.entrySet().stream().map(entry -> {
            PieDataVO pieDataVO = new PieDataVO();
            pieDataVO.setName(map.get(entry.getKey()));
            pieDataVO.setValue(entry.getValue().stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add));
            return pieDataVO;
        }).toList();
    }

    @GetMapping("expPieData")
    public List<PieDataVO> expDataPie() {
        // 获取所有支出科目
        List<AccountCls> all = accountClsRepository.findAllByParentIdOrderByOrderNumAsc(4L);
        Map<String, String> map = all.stream().collect(Collectors.toMap(AccountCls::getCode, AccountCls::getName));
        List<String> list = all.stream().map(AccountCls::getCode).toList();
        List<VoucherDetail> allByAcctClsCodeIn = voucherDetailRepository.findAllByAcctClsCodeIn(list);
        // 按照科目分组统计每个科目下合计, 并写入到PieData
        Map<String, List<VoucherDetail>> collect = allByAcctClsCodeIn.stream().collect(Collectors.groupingBy(VoucherDetail::getAcctClsCode));
        return collect.entrySet().stream().map(entry -> {
            PieDataVO pieDataVO = new PieDataVO();
            pieDataVO.setName(map.get(entry.getKey()));
            pieDataVO.setValue(entry.getValue().stream().map(VoucherDetail::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add));
            return pieDataVO;
        }).toList();
    }
}
