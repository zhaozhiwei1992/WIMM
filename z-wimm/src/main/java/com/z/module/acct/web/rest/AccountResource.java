package com.z.module.acct.web.rest;

import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.repository.VoucherDetailRepository;
import com.z.module.acct.web.vo.AccountVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/acct")
public class AccountResource {

    private final VoucherDetailRepository voucherDetailRepository;
    private final AccountClsRepository accountClsRepository;

    public AccountResource(VoucherDetailRepository voucherDetailRepository, AccountClsRepository accountClsRepository) {
        this.voucherDetailRepository = voucherDetailRepository;
        this.accountClsRepository = accountClsRepository;
    }

    @Operation(description = "记账")
    @PostMapping("/account")
    @Transactional
    public String acct(@RequestBody AccountVO accountVO){

        long count = voucherDetailRepository.count();
        String voucherNo = String.format("%010d", count/2);

        List<AccountCls> accountClsList = accountClsRepository.findAllByCodeIn(Arrays.asList(accountVO.getCreditAccount(), accountVO.getDebitAccount()));
        Map<String, String> map = accountClsList.stream().collect(Collectors.toMap(AccountCls::getCode, AccountCls::getName));

        ArrayList<VoucherDetail> voucherDetails = new ArrayList<>();

        // 根据记账的操作分别生成贷方借方分录
        {
            VoucherDetail voucherDetail = new VoucherDetail();
            voucherDetail.setAcctClsCode(accountVO.getCreditAccount());
            voucherDetail.setAcctClsName(map.get(accountVO.getCreditAccount()));
            voucherDetail.setAmt(accountVO.getAmt());
            voucherDetail.setRemark(accountVO.getRemark());
            voucherDetail.setDrCr(-1);
            voucherDetail.setVoucherNo(voucherNo);
            voucherDetails.add(voucherDetail);
        }
        {
            VoucherDetail voucherDetail = new VoucherDetail();
            voucherDetail.setAcctClsCode(accountVO.getDebitAccount());
            voucherDetail.setAcctClsName(map.get(accountVO.getDebitAccount()));
            voucherDetail.setAmt(accountVO.getAmt());
            voucherDetail.setRemark(accountVO.getRemark());
            voucherDetail.setDrCr(1);
            voucherDetail.setVoucherNo(voucherNo);
            voucherDetails.add(voucherDetail);
        }

        // 入库
        voucherDetailRepository.saveAll(voucherDetails);

        // TODO 异步操作可以进行金额等计算，方便做统计报表的性能。不用mq自己模拟一个消息即可
        return "success";
    }
}
