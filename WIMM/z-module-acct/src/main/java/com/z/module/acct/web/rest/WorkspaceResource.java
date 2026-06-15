package com.z.module.acct.web.rest;

import com.z.module.acct.service.WorkspaceService;
import com.z.module.acct.web.vo.AssetVO;
import com.z.module.acct.web.vo.LineDataVO;
import com.z.module.acct.web.vo.PieDataVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: WorkspaceResource
 * @Package com/z/module/acct/web/rest/WorkspaceResource.java
 * @Description: 工作台信息加载
 * @date 2024/10/12 17:01
 */
@Tag(name = "工作台API")
@RestController
@RequestMapping(value = "/acct/workplace")
@Slf4j
public class WorkspaceResource {

    private final WorkspaceService workspaceService;

    public WorkspaceResource(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping("asset")
    public AssetVO asset() {
        return workspaceService.getAsset();
    }

    @GetMapping("monthlyIncExpData")
    public List<LineDataVO> monthlyIncExpData() {
        return workspaceService.getMonthlyIncExpData();
    }

    @GetMapping("incPieData")
    public List<PieDataVO> incPieData() {
        return workspaceService.getIncPieData();
    }

    @GetMapping("expPieData")
    public List<PieDataVO> expPieData() {
        return workspaceService.getExpPieData();
    }
}
