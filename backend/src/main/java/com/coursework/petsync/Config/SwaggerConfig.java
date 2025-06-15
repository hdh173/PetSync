package com.coursework.petsync.Config;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置 OpenAPI 信息
     * @return OpenAPI 实例
     */
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PetSync 后端接口文档")
                        .description("2025.5-6")
                        .version("v1.0"));
    }
}