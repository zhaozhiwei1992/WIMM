package com.z.module.acct.web.rest;

import com.alibaba.excel.EasyExcel;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.service.VoucherDetailService;
import com.z.module.acct.web.vo.VoucherDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: VoucherDetailResource
 * @Package com/z/module/acct/web/rest/VoucherDetailResource.java
 * @Description: 凭证分录接口
 * @date 2024/10/28 16:53
 */
@Tag(name = "凭证分录API")
@RestController
@RequestMapping("/acct")
@Slf4j
public class VoucherDetailResource {

    private final VoucherDetailService voucherDetailService;

    public VoucherDetailResource(VoucherDetailService voucherDetailService) {
        this.voucherDetailService = voucherDetailService;
    }

    @Operation(description = "获取所有会计分录")
    @GetMapping("/voucher/detail")
//    @PreAuthorize("hasAuthority('acct:VoucherDetail:view')")
    public Map<String, Object> getAllVoucherDetail(Pageable pageable, VoucherDetail voucherDetail) {
        return voucherDetailService.getAllVoucherDetail(pageable, voucherDetail);
    }

    @Operation(description = "删除会计分录")
    @DeleteMapping("/voucher/detail")
//    @PreAuthorize("hasAuthority('acct:VoucherDetail:delete')")
    public String deleteVoucherDetail(@RequestBody List<Long> idList) {
        voucherDetailService.deleteByVoucherNo(idList);
        return "success";
    }

    @Operation(description = "导出记账明细")
    @GetMapping("/voucher/export")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("记账明细", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<VoucherDetailVO> data = voucherDetailService.getAllForExport();
        EasyExcel.write(response.getOutputStream(), VoucherDetailVO.class)
                .sheet("记账明细")
                .doWrite(data);
    }
}
