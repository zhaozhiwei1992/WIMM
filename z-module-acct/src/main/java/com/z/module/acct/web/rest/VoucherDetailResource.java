package com.z.module.acct.web.rest;

import com.z.framework.security.util.SecurityUtils;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.VoucherDetailRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: AccountClsResource
 * @Package com/z/module/acct/web/rest/AccountClsResource.java
 * @Description: 分录维护
 * @date 2024/10/28 16:53
 */
@Tag(name = "分录API")
@RestController
@RequestMapping("/acct")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class VoucherDetailResource {

    private final VoucherDetailRepository voucherDetailRepository;

    public VoucherDetailResource(VoucherDetailRepository voucherDetailRepository) {
        this.voucherDetailRepository = voucherDetailRepository;
    }

    /**
     * {@code GET  /VoucherDetail} : get all the accountCls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of VoucherDetail in body.
     */
    @Operation(description = "获取所有会计分录")
    @GetMapping("/voucher/detail")
//    @PreAuthorize("hasAuthority('acct:VoucherDetail:view')")
    public HashMap<String, Object> getAllVoucherDetail(
            Pageable pageable, VoucherDetail voucherDetail) {
        log.debug("REST request to get a page of UiComponents");

        //创建匹配器，即如何使用查询条件
        //构建对象
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true)
                .withIgnoreNullValues()
                //名字采用“开始匹配”的方式查询
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                //忽略属性：是否关注。因为是基本类型，需要忽略掉
                .withIgnorePaths("id", "createdDate", "lastModifiedDate");

        String currentLoginName = SecurityUtils.getCurrentLoginName();
        if(!"admin".equals(currentLoginName)){
            voucherDetail.setCreatedBy(currentLoginName);
        }

        //创建实例
        Example<VoucherDetail> ex = Example.of(voucherDetail, matcher);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "voucherNo", "drCr");
        // 分页
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), sort);
//        Page<VoucherDetail> all = voucherDetailRepository.findAll(ex, pageable);
        Page<VoucherDetail> all = voucherDetailRepository.findAll(pageable);

        return new HashMap<>() {{
            put("list", all.getContent());
            put("total", all.getTotalElements());
        }};

    }

    @Operation(description = "删除会计分录")
    @DeleteMapping("/voucher/detail")
//    @PreAuthorize("hasAuthority('acct:VoucherDetail:delete')")
    public String deleteAccountCls(@RequestBody List<Long> idList) {
        log.debug("REST request to delete Examples, ids: {}", idList);
        List<VoucherDetail> allById = voucherDetailRepository.findAllById(idList);
        List<String> list = allById.stream().map(VoucherDetail::getVoucherNo).toList();
        voucherDetailRepository.deleteAllByVoucherNoIn(list);
        return "success";
    }

}
