package com.security.security.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication")
                        .addList("Basic Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
                        .addSecuritySchemes("Basic Authentication", createAPIBasicScheme()))
                .info(apiInfo());
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private SecurityScheme createAPIBasicScheme(){
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");
    }

    private Info apiInfo() {
        return new Info()
                .title(apiTitle())
                .description(apiDescription())
                .version("1.0.0")
                .contact(apiContact())
                .license(apiLicence());
    }

    private String apiTitle(){
        return "Spring Security";
    }

    private String apiDescription(){
        return "Spring Security App Default";
    }

    private License apiLicence() {
        return new License()
                .name("MIT Licence")
                .url("https://opensource.org/licenses/mit-license.php");
    }

    private Contact apiContact() {
        return new Contact()
                .name("Spring Security")
                .email("levi.pereira.junior@ccc.ufcg.edu.br")
                .url("https://github.com/LeviJunior21/SpringSecurity");
    }
}
