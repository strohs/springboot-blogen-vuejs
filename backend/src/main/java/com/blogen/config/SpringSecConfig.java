package com.blogen.config;

import com.blogen.services.security.RestApiAccessDeniedHandler;
import com.blogen.services.security.RestApiAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


/**
 * Blogen Security Configuration.
 * <p>
 * In this example project, Spring Boot is performing four roles: Rest Server, OAuth2 Login server,
 * OAuth2 Resource Server, and HTTP Server.
 * <p>
 * Therefore, this configuration enables support for
 * - enabling users to login using a username and password
 * - accepting a Blogen issued JWT as an authentication/authorization mechanism when accessing the REST Api endpoints
 * located at /api/v1/**   NOTE that we are intentionally leaving the "/api/v1/auth/**" path unsecured so that users
 * can log in, or register as a new user without first needing a JWT
 * - enabling support for using Spring Security as an OAuth2 login server
 * - enabling support for using Spring Security as a OAuth2 resource server so that we can leverage its functionality
 * to create, accept and validate our own JWTs
 * - ignoring paths that serve .html, .css, .js, images etc... since the Vue.js frontend client will be requesting these
 * <p>
 * After a successful login, any JWT tokens received from an OAuth2 provider will be converted into our own custom
 * JWT be copying relevant claims from the provider. This is done in the OAuth2UserLoginServiceImpl class
 *
 * @author Cliff
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecConfig extends WebSecurityConfigurerAdapter {


    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;


    @Bean
    // used to encode user passwords
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
        // used to enable decoding JWTs using our public key
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
        // used to encode and self sign or JWTs using a private key
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //these matchers will allow swagger UI, h2-console, and image files through spring-security
        web.ignoring()
                .antMatchers("/v3/api-docs/**",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/console/*",
                        "/h2-console/**",
                        "/actuator/**",
                        "/favicon.ico",
                        "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
                        "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.map");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .logout((configurer) -> configurer.logoutSuccessUrl("/"))
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/", "/api/v1/auth/**", "/login/form").permitAll()
                        .antMatchers("/api/**").hasAuthority("SCOPE_ROLE_API")
                        .anyRequest().authenticated()
                )
                .oauth2Login((c) -> c.defaultSuccessUrl("/login/oauth2/success", true))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                //.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new RestApiAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestApiAccessDeniedHandler())
                );
    }

//    // NOTE: we can't configure a custom SecurityFilterChain when using WebSecurityConfigurerAdapter, so we are
//    // using the HttpSecurity configurer above
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .and()
//                .csrf(AbstractHttpConfigurer::disable)
//                .logout((configurer) -> configurer.logoutSuccessUrl("/"))
//                .authorizeHttpRequests((authorize) -> authorize
//                        .antMatchers("/","/api/v1/auth/**", "/login/form").permitAll()
//                        .antMatchers("/api/**").hasRole("API")
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login((c) -> c.defaultSuccessUrl("/login/oauth2/success", true))
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling((exceptions) -> exceptions
//                        .authenticationEntryPoint(new RestApiAuthenticationEntryPoint())
//                        .accessDeniedHandler(new RestApiAccessDeniedHandler())
//                );
//        return http.build();
//    }


}
