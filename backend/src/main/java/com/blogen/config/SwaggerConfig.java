package com.blogen.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * Config for OpenAPI (Swagger) and swagger-ui
 * Using this to document the REST API
 *
 * @author Cliff
 */
@Configuration
public class SwaggerConfig {

    // We only have one docket for the Blogen API, so we can do configuration
    // in application.properties instead of the bean below:
    // springdoc.packagesToScan=com.blogen.api
    // springdoc.pathsToMatch=/api/**

//    @Bean
//    public GroupedOpenApi api() {
//        return GroupedOpenApi.builder()
//                .group("blogen-public")
//                .pathsToMatch("/api/**")
//                .build();
//    }

    @Bean
    public OpenAPI blogenOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Blogen API")
                        .description("Blogen sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://github.com/strohs")))

                // enable support in the swagger-ui for JWT Bearer tokens in the Authorization header
                // this config will turn on authorization across the whole REST Api. To disable authentication
                // for a particular endpoint, you must annotate the controller method with @SecurityRequirements
                .components(new Components()
                        //HTTP Bearer with JWT, see: https://swagger.io/docs/specification/authentication/basic-authentication/
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("jwt")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth")
                );
    }

}
