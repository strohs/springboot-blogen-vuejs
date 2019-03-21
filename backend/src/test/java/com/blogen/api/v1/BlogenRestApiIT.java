package com.blogen.api.v1;

import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.services.security.BlogenJwtService;
import com.blogen.services.security.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * some integration tests with the Blogen Rest API
 */
@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@AutoConfigureTestDatabase
public class BlogenRestApiIT {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;


    @Test
    public void should_be_able_to_call_latestPosts_without_jwt() {
        PostListDTO latestPosts = restTemplate.getForObject("/api/v1/auth/latestPosts", PostListDTO.class);
        assertThat( latestPosts, is( notNullValue()) );
        assertThat( latestPosts.getPosts().size(), greaterThan(0) );
    }

    @Test
    public void should_authenticate_user_with_valid_jwt_when_GET_users_authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( buildValidJwt("1", "API","USER") );
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        final ResponseEntity<UserDTO> re = restTemplate.exchange("/api/v1/users/authenticate", HttpMethod.GET, httpEntity, UserDTO.class);

        assertThat( re.getStatusCode(), is(HttpStatus.OK) );
        assertThat( re.getBody().getId(), is(1L) );
    }

    @Test
    public void should_return_UNAUTHORIZED_when_user_with_invalid_jwt_calls_api() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( buildExpiredJwt() );
        RequestEntity requestEntity = new RequestEntity(
                headers,
                HttpMethod.GET,
                URI.create("/api/v1/users/authenticate"));

        final ResponseEntity<UserDTO> re = restTemplate.exchange( requestEntity, UserDTO.class);

        assertThat( re.getStatusCode(), is(HttpStatus.UNAUTHORIZED) );
    }

    private String buildExpiredJwt() {
        BlogenJwtService service = (BlogenJwtService) jwtService;
        return service.builder()
                .withSubject( "1" )
                .withScopes(Arrays.asList("API","USER") )
                .withIssuedAt(LocalDateTime.of(1970,1,1,1,1).toInstant(ZoneOffset.UTC))
                .withExpirationMs(1)
                .buildToken();
    }

    private String buildValidJwt(String sub, String ... scopes) {
        BlogenJwtService service = (BlogenJwtService) jwtService;
        return service.builder()
                .withSubject( sub )
                .withScopes(Arrays.asList(scopes))
                .withIssuedAt( Instant.now() )
                .withExpirationMs(180000)
                .buildToken();
    }


}
