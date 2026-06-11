package com.example.swedemo.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coding Test Platform API")
                        .description("온라인 코딩 테스트 플랫폼 백엔드 API")
                        .version("v1.0.0"));
    }
}
