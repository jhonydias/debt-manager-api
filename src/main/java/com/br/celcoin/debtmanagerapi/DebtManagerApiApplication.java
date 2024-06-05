package com.br.celcoin.debtmanagerapi;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class DebtManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebtManagerApiApplication.class, args);
    }

}
