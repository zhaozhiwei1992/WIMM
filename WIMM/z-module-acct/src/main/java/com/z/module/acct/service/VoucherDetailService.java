package com.z.module.acct.service;

import com.z.framework.security.util.SecurityUtils;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.VoucherDetailRepository;
import com.z.module.acct.web.mapper.VoucherDetailMapper;
import com.z.module.acct.web.vo.VoucherDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凭证分录 Service
 */
@Service
@Slf4j
public class VoucherDetailService {

    private final VoucherDetailRepository voucherDetailRepository;
    private final VoucherDetailMapper voucherDetailMapper;

    public VoucherDetailService(VoucherDetailRepository voucherDetailRepository,
                                VoucherDetailMapper voucherDetailMapper) {
        this.voucherDetailRepository = voucherDetailRepository;
        this.voucherDetailMapper = voucherDetailMapper;
    }

    /**
     * 分页查询凭证分录
     */
    public Map<String, Object> getAllVoucherDetail(Pageable pageable, VoucherDetail voucherDetail) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true)
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id", "createdDate", "lastModifiedDate");

        String currentLoginName = SecurityUtils.getCurrentLoginName();
        if (!"admin".equals(currentLoginName)) {
            voucherDetail.setCreatedBy(currentLoginName);
        }

        Example<VoucherDetail> ex = Example.of(voucherDetail, matcher);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "voucherNo", "drCr");
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), sort);
        Page<VoucherDetail> all = voucherDetailRepository.findAll(ex, pageable);

        return new HashMap<>() {{
            put("list", all.getContent());
            put("total", all.getTotalElements());
        }};
    }

    /**
     * 按凭证号批量删除分录
     */
    public void deleteByVoucherNo(List<Long> idList) {
        List<VoucherDetail> allById = voucherDetailRepository.findAllById(idList);
        List<String> voucherNos = allById.stream().map(VoucherDetail::getVoucherNo).toList();
        voucherDetailRepository.deleteAllByVoucherNoIn(voucherNos);
    }

    /**
     * 查询所有分录用于导出
     */
    public List<VoucherDetailVO> getAllForExport() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "voucherNo", "drCr");
        List<VoucherDetail> all = voucherDetailRepository.findAll(sort);
        return voucherDetailMapper.convert(all);
    }
}
