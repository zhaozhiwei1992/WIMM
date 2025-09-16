package com.z.module.acct.web.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class AccountClsVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    private String setYear; // 核算年度
    private String code; // 预算指标核算科目代码
    private String name; // 预算指标核算科目名称
    private Integer balanceDir; // 余额方向
    private Integer isEnabled; // 是否启用
    private String remark; // 备注
    private Integer isStandard; // 是否标准
    private Long parentId; // 上级科目唯一标识
    private Integer levelNo; // 级次
    private Integer isLeaf; // 是否末级
    private Integer isLastInst; // 是否终审
    private String icon; // 图标
    private List<AccountClsVO> children;
}
