package com.blogen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Config for OpenAPI (Swagger)
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
                        .license(new License().name("Apache 2.0").url("http://github.com/strohs")));
    }


//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex("/api/v1.*"))
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("apiKey", authorizationScopes));
//    }
//
//    @Bean
//    public SecurityConfiguration security() {
//        return SecurityConfigurationBuilder.builder()
//                .clientId("test-app-client-id")
//                .clientSecret("test-app-client-secret")
//                .realm("test-app-realm")
//                .appName("test-app")
//                .scopeSeparator(",")
//                .additionalQueryStringParams(null)
//                .useBasicAuthenticationWithAccessCodeGrant(false)
//                .build();
//    }
}
