package com.authentication.jwt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Employee JWT API",
                version = "1.0",
                description = "Employee Management REST API"
        )
)
public class OpenApiConfig {
}