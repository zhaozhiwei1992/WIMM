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
import java.util.Objects;

/**
 * 凭证分录 Service
 * <p>
 * 多租户隔离: 查询/删除均限定当前家庭的 tenant_id.
 * 不再依赖 CustomStatementInspector 的 SQL 改写(避免占位符/重写风险).
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
     * 分页查询凭证分录(当前家庭)
     */
    public Map<String, Object> getAllVoucherDetail(Pageable pageable, VoucherDetail voucherDetail) {
        // 强制按当前家庭隔离
        voucherDetail.setTenantId(SecurityUtils.getTenantId());

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true)
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id", "createdDate", "lastModifiedDate");

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
     * 按ID批量删除所属交易的整笔凭证(校验归属)
     */
    public void deleteByVoucherNo(List<Long> idList) {
        String tenantId = SecurityUtils.getTenantId();
        // 仅取属于当前家庭的分录, 防止越权删除
        List<VoucherDetail> allById = voucherDetailRepository.findAllById(idList).stream()
                .filter(v -> Objects.equals(v.getTenantId(), tenantId))
                .toList();
        if (allById.isEmpty()) {
            return;
        }
        List<String> voucherNos = allById.stream().map(VoucherDetail::getVoucherNo).toList();
        voucherDetailRepository.deleteAllByVoucherNoInAndTenantId(voucherNos, tenantId);
    }

    /**
     * 查询所有分录用于导出(当前家庭)
     */
    public List<VoucherDetailVO> getAllForExport() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "voucherNo", "drCr");
        List<VoucherDetail> all = voucherDetailRepository
                .findAllByTenantIdOrderByCreatedDateDesc(SecurityUtils.getTenantId(), sort);
        return voucherDetailMapper.convert(all);
    }
}
