package com.oni.training.springboot.MyProduct.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        Info info=new Info().title("My SpringBoot4Learn API Document")
                .version("Ver 1.0.0")
                .description("I have practiced before");

        String securitySchemeName="JWT Authentication";
        SecurityRequirement securityRequirement=
                new SecurityRequirement().addList(securitySchemeName);
        Components components=new Components()
                .addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
