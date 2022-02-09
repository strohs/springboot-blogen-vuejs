package com.blogen.services.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class BlogenJwtServiceITest {

    @Autowired
    private JwtService blogenJwtService;

    @Autowired
    private JwtDecoder jwtDecoder;


    @Test
    public void should_generate_a_jwt_string() throws Exception {
        String sub = "1";
        List<BlogenAuthority> scopes = List.of(BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_USER);

        String jwt = blogenJwtService.generateToken(sub, scopes, Instant.now(), 1800L);

        assertThat( jwt, is( notNullValue() ));
    }

    @Test
    public void should_decode_a_jwt_with_subject_eq_3() throws Exception {
        String sub = "3";
        List<BlogenAuthority> scopes = List.of(BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_USER);

        String jwt = blogenJwtService.generateToken(sub, scopes, Instant.now(), 1800L);

        Jwt decodedJwt = jwtDecoder.decode(jwt);
        assertThat( decodedJwt.getSubject(), is( "3" ));

    }

    @Test
    public void decoded_jwt_should_have_scope_with_2_claims() throws Exception {
        String sub = "3";
        List<BlogenAuthority> scopes = List.of(BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_USER);

        String jwt = blogenJwtService.generateToken(sub, scopes, Instant.now(), 1800L);
        Jwt decodedJwt = jwtDecoder.decode(jwt);

        assertThat(decodedJwt.getClaim("scope"), is(notNullValue()));
        String claimStr = decodedJwt.getClaim("scope");
        List<String> claims = List.of(claimStr.split(" "));
        assertThat(claims.size(), is(2));
        assertThat(claims, contains(equalTo(BlogenAuthority.ROLE_API.toString()), equalTo(BlogenAuthority.ROLE_USER.toString())));

    }



}