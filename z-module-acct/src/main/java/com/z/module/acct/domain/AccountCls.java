package com.z.module.acct.domain;

import com.z.framework.common.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Title: AccountCls
 * @Package com/z/module/acct/domain/AccountCls.java
 * @Description: 会计科目(账户)维护
 * @author zhaozhiwei
 * @date 2024/10/28 16:36
 * @version V1.0
 */
@Entity
@Table(name = "acct_account_cls")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountCls extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 预算指标核算科目唯一标识

    private String setYear; // 核算年度

    private String code; // 预算指标核算科目代码

    private String name; // 预算指标核算科目名称

    private Integer isEnabled; // 是否启用

    private String remark; // 备注

    private Integer isStandard; // 是否标准

    private Long parentId; // 上级科目唯一标识

    private Integer levelNo; // 级次

    private Integer isLeaf; // 是否末级

    private String icon; // 图标

    private Integer orderNum; // 排序
}
