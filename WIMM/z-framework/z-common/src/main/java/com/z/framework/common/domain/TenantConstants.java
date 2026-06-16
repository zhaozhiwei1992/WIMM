package com.z.framework.common.domain;

/**
 * 租户(家庭)相关常量.
 *
 * @author zhaozhiwei
 */
public final class TenantConstants {

    private TenantConstants() {
    }

    /**
     * 预设科目模板的租户标识.
     * 模板数据存放于此 tenant_id 下, 仅供注册时复制使用,
     * 不出现在任何业务查询结果中, 删除用户时也不参与清理.
     */
    public static final String TEMPLATE_TENANT_ID = "__template__";
}
