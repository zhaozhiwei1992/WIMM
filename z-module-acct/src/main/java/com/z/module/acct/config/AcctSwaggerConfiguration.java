package com.z.module.acct.config;

import com.z.framework.common.config.SwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AcctSwaggerConfiguration {

    /**
     * system 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi acctGroupedOpenApi() {
        return SwaggerAutoConfiguration.buildGroupedOpenApi("acct");
    }

}
