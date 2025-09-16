package com.z.framework.common.domain;

import java.util.Objects;

public class TenantContext {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        CONTEXT.set(tenantId);
    }

    public static String getTenantId() {
        if(Objects.isNull(CONTEXT.get())){
            return "nl";
        }else{
            return CONTEXT.get();
        }
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
