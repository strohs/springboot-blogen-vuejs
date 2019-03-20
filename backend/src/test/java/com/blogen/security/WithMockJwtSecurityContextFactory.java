package com.blogen.security;


import com.blogen.services.security.BlogenJwtService;
import com.blogen.services.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();


        // if issuedAt time was not set, default it to the Instant.now()
        Instant issuedAt = annotation.issuedAtMs() == 0 ? Instant.now() : Instant.ofEpochMilli( annotation.issuedAtMs() );

        // build a JWT string with the specified subject and scopes
        String jwtStr = ((BlogenJwtService) jwtService).builder()
                .withSubject( annotation.subject() )
                .withIssuedAt( issuedAt )
                .withScopes( Arrays.asList( annotation.scopes() ))
                .buildToken();
        // decode the jwt string into Spring Security's Jwt object
        final Jwt jwt = jwtDecoder.decode(jwtStr);

        // store authorities with "SCOPE_" prepended in order to indicate these are authorities from JWT scopes
        List<GrantedAuthority> authorities = jwt.getClaimAsStringList("scope").stream()
                .map(claim -> new SimpleGrantedAuthority("SCOPE_"+claim))
                .collect(Collectors.toList());

        // store the Jwt as the authenticated principal
        Authentication auth = new JwtAuthenticationToken( jwt, authorities );
        context.setAuthentication( auth );
        return context;
    }

}
