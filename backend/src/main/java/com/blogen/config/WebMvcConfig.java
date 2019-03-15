package com.blogen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This configuration will enable cross origin requests Globally. This is typically needed by
 * javascript single page applications that run on their own (separate) development servers
 *
 * Author: Cliff
 */
@Configuration
@Profile( {"dev"} )
public class WebMvcConfig implements WebMvcConfigurer {

    // Note that CORS has also been enabled in the SpringSecConfig
    @Override
    public void addCorsMappings( CorsRegistry registry ) {
        long MAX_AGE_SECS = 3600;
        registry.addMapping( "/**" )
                .allowedOrigins( "*" )
                .allowedMethods( "HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE" )
                .exposedHeaders("Authorization")
                .maxAge(MAX_AGE_SECS);
    }
}
