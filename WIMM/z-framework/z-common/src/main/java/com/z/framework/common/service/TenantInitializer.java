package com.z.framework.common.service;

/**
 * 租户(家庭)初始化扩展点.
 * <p>
 * 新家庭(租户)注册时, 系统模块会调用所有实现该接口的 bean,
 * 各业务模块可据此为该租户初始化自己的数据(如预设科目、默认配置等).
 * <p>
 * 用 SPI 解耦: z-module-system 不反向依赖具体业务模块(如 z-module-acct).
 * 参考 {@link MenuRouterExtService} 的扩展模式.
 *
 * @author zhaozhiwei
 */
public interface TenantInitializer {

    /**
     * 为指定租户初始化数据. 在注册事务内同步执行.
     *
     * @param tenantId 新家庭的租户标识
     */
    void initialize(String tenantId);

}
