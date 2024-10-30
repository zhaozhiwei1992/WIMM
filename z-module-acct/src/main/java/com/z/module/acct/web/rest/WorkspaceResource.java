package com.z.module.acct.web.rest;

import com.z.module.acct.web.vo.AssetVO;
import com.z.module.system.web.vo.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: WorkspaceResource
 * @Package com/z/module/system/web/rest/WorkspaceResource.java
 * @Description: 工作台信息加载
 * @author zhaozhiwei
 * @date 2024/10/12 17:01
 * @version V1.0
 */

@Tag(name = "工作台API")
@RestController
@RequestMapping(value = "/acct/workplace")
@Slf4j
public class WorkspaceResource {


    @GetMapping("asset")
    public AssetVO asset(){
        AssetVO assetVO = new AssetVO();
        return assetVO;
    }
}
