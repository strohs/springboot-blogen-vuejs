package com.blogen.services.security;

import com.blogen.services.security.WithMockJwtSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {

    long issuedAtMs() default 0L;
    int expirationMs() default 180000;
    String subject();
    String [] scopes();
}
