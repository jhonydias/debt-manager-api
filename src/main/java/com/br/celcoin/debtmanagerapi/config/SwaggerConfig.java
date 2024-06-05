package com.br.celcoin.debtmanagerapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${app.config.version}") String appVersion,
                                 @Value("${spring.profiles.active}") String activeProfile,
                                 @Value("${app.config.name}") String appName
    ) {
        OpenAPI openAPI = new OpenAPI();

        openAPI.info(new Info()
                .title(appName)
                .version("v" + appVersion + " - " + activeProfile)
        );

        return openAPI;
    }
}
