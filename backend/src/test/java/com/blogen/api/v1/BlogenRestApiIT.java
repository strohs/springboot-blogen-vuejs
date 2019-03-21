package com.blogen.api.v1;

import com.blogen.api.v1.model.PostListDTO;
import com.blogen.services.security.BlogenJwtService;
import com.blogen.services.security.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * integration tests with the Blogen Rest API
 */
@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@AutoConfigureTestDatabase
public class BlogenRestApiIT {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    private String validJwt = "";
    private String expiredJwt = "";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_get_9_posts_when_latestPosts() {
        PostListDTO latestPosts = restTemplate.getForObject("/api/v1/auth/latestPosts", PostListDTO.class);
        assertThat( latestPosts, is( notNullValue()) );
        assertThat( latestPosts.getPosts().size(), is(9) );
    }

    private String buildExpiredJwt() {
        BlogenJwtService service = (BlogenJwtService) jwtService;
        return service.builder()
                .withSubject( "1" )
                .withScopes(Arrays.asList("API","USER") )
                .withIssuedAt(Instant.MIN)
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
