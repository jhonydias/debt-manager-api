package com.br.celcoin.debtmanagerapi.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Jhony Dias
 * See more {@link net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock}
 * See the implementation {<a href="https://www.baeldung.com/shedlock-spring">...</a>}
 */

@Configuration
public class SchedulerConfiguration {
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }
}