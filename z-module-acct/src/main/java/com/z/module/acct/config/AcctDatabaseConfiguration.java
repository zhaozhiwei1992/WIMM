package com.z.module.acct.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({ "com.z.module.acct.repository" })
@EntityScan({"com.z.module.acct.domain"})
public class AcctDatabaseConfiguration {}
