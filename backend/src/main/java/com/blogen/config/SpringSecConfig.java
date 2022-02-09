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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


/**
 * Blogen Security Configuration
 *
 * @author Cliff
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier("daoAuthenticationProvider")
//    private AuthenticationProvider daoAuthenticationProvider;
//
//    // AuthenticationProvider used to authenticate username/passwords from the Blogen login form
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider( UserDetailsService userDetailsService ){
//
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder() );
//        daoAuthenticationProvider.setUserDetailsService( userDetailsService );
//        return daoAuthenticationProvider;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //super.configure(auth);
//        auth.authenticationProvider( daoAuthenticationProvider );
//    }

//    // EntryPoint that returns a HTTP 401 response if a JWT is invalid/expired
//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizedHandler;

//    // RSA public key used to verify JWTs signed by blogen
//    @Value("${app.jwtPublicKey}")
//    private String jwtPublicKey;


//    //TODO do we still need to expose this bean?
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    // this bean is used by Spring to verify the signature of JWTs
//    @Bean
//    JwtDecoder jwtDecoder() throws Exception {
//        return NimbusJwtDecoder.withPublicKey( key() ).build();
//    }


//    //generates the PublicKey used to verify the JWTs used in Blogen
//    private RSAPublicKey key() throws Exception {
//        byte[] bytes = Base64.getDecoder().decode(jwtPublicKey.getBytes());
//        return (RSAPublicKey) KeyFactory.getInstance("RSA")
//                .generatePublic(new X509EncodedKeySpec(bytes));
//    }

    //    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .cors()
//                    .and()
//                .csrf().disable()
//                .logout()
//                    .logoutSuccessUrl("/")
//                    .and()
//                .authorizeRequests()
//                    .antMatchers("/","/api/v1/auth/**", "/login/form")
//                        .permitAll()
//                    .antMatchers("/api/**")
//                        .hasAuthority("SCOPE_API") // this is how the Resource Server authorizes API requests
//                    .anyRequest()
//                        .authenticated()
//                    .and()
//                        .oauth2Login()
//                            .defaultSuccessUrl("/login/oauth2/success", true)
//                    .and()
//                .oauth2ResourceServer()
//                    .authenticationEntryPoint(unauthorizedHandler)
//                    .jwt()
//                        .decoder(jwtDecoder());
//    }

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //these matchers will allow swagger UI, h2-console, and image files through spring-security
        web.ignoring()
                .antMatchers("/v3/api-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/configuration/security",
                        "/swagger-ui.html",
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
