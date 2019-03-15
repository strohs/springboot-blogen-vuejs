package com.blogen.config;

import com.blogen.services.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtProcessors;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Spring Security Configuration - original NON-REST API security config
 *
 * @author Cliff
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("daoAuthenticationProvider")
    private AuthenticationProvider daoAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider( UserDetailsService userDetailsService ){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder() );
        daoAuthenticationProvider.setUserDetailsService( userDetailsService );
        return daoAuthenticationProvider;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        auth.authenticationProvider( daoAuthenticationProvider );
    }

    @Value("${app.jwtPublicKey}")
    private String jwtPublicKey;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    JwtDecoder jwtDecoder() throws Exception {
        // decodes a JWT from compact claims representation format into a Jwt object
        return new NimbusJwtDecoder(JwtProcessors.withPublicKey(key()).build());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //this will allow swagger UI, h2-console, and image files through spring-security
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources",
                "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**", "/console/*",
                "/h2-console/**", "/actuator/**", "/favicon.ico",
                "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.map");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                    .and()
                .csrf().disable()
                .logout()
                    .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                    .antMatchers("/","/api/v1/auth/**","/blogen/login/form/**")
                        .permitAll()
                    .antMatchers("/api/**")
                        .hasAuthority("SCOPE_API")
                    .anyRequest()
                        .authenticated()
                    .and()
                        .oauth2Login()
                .and()
                .oauth2ResourceServer()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .jwt()
                        .decoder(jwtDecoder());
    }

    //generates the PublicKey used to verify the JWTs used in Blogen
    private RSAPublicKey key() throws Exception {
        byte[] bytes = Base64.getDecoder().decode(jwtPublicKey.getBytes());
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(bytes));
    }

}
