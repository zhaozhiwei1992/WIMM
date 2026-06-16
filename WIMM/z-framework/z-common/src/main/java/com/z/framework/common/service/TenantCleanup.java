package com.z.framework.common.service;

/**
 * 租户(家庭)清理扩展点.
 * <p>
 * 当某家庭最后一个用户被删除, 即该 tenant_id 下已无任何用户时,
 * 系统模块会调用所有实现该接口的 bean, 清理该租户的业务数据.
 * <p>
 * 与 {@link TenantInitializer} 对称, 用 SPI 解耦, 避免反向依赖.
 *
 * @author zhaozhiwei
 */
public interface TenantCleanup {

    /**
     * 清理指定租户的所有业务数据(科目、凭证等).
     *
     * @param tenantId 已无用户归属的家庭标识
     */
    void cleanup(String tenantId);

}
