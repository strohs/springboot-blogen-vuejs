package com.blogen.services.security;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BlogenJwtServiceTest {

    private JwtService blogenJwtService;

    String jwtPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDO8kOT1lSolexQOyDKqA9ljzrCBAMtY7UFfSLuhe/zabU5oq76HbzYFLTcffSiO8DwO0go8srIdkl2miWeAuD/HuBG1dRxLgHA/FXcewfYWx6dsrdhUz7sk01OTUPG7hPiI0d+YMqGhprcn9QwCHKS6986fPAaHiy0livTy4c2k+4OGGyVUysSI4/Fb9FZH4DwolIxRvOpCl+knPJ6fQn/NyUdLyO+NewhtLmPB4vQOFHoiJTuf3CLMKV/XU/9k1RtT53MLpioTH2IouL/MtdIkT6Vjri9faire7kbwYDCNvfhYVVrRmJ48Cew220kbsj8js/fgJnAatVETEZDFnrnAgMBAAECggEAaxMUfgiWkwfHVpfRVqnwFXcvSrVyw2hkgCuICGwKhCI8n06aRFVgoVjNX4xYKBzMrj+uDWnaHDB/wDrvnbsVmUrrdhsrKUhJpgWpk0OhAoPa6nNOyK+ezh9FD2+Q0vvaE9Z3Jl+ESmiY9fBIhHXKelRKkuOUA597OBC/Kn2TP1wKCudYd727Z+Eko1dYFIHVR5zDpT2Ooc7gggSyKhbPvypt/f0D1K8p5j/Z00lQtPdL61v5JrimU9CRx7tdCTgEORT4HK1DV9s54UMdI1tkx4EliW4QHXqeDyFtLNMSlvYOD3jmA/rO4+nBos1pam5ohVdgLQPWiZ8H8FmyKVqLUQKBgQD5bBidKZDi+rTM4776pHFssYbN1XrhlRTHNrfshGVPXihB9n/L4n8XH703sPjYx80sK9s4uwVeX27vQZbhB6SSi3UV266RoCOJw3YooIMNe+GNRS2Du8Ltni8uoPvsHboMFMbId+6QNXQYcWIjE59CPrUc48KBOqlTjfcnGsOSXwKBgQDUZ2dbpZpCjZR9hvO5oiGY72vCjWOZl0IrN6BdnS5XY7s0xkDsgUUR7CSMOXWnTmNszd5Up11vVlZaENTHiyKmbGzoum1KBrqSPZVocgZX95lQtGhaOOdveYEGCOQ4ri1D0PXMlGSQ6o2CmCa3rgSGZ1zcwKECr35Rgzlu8vU0eQKBgCdyhi4NDeIoHoItHt5LNbkoFpblRYr55hBfwhUBF3jG/nz4PFwFlW4V0fpF3gWDDBsJUKGXSpETbNsgN3mdmNSog7sSRHi5qAU4ya5BPJjcV97c6wbjoj+sfe/U/CLqzKruekXoWe2q1pKrFAdSKiTy//fyuadwnKkfUdB4difnAoGAQlLHg2aCdKWg6FFraVunAgpAq89aemswzi3gdVrPTZpmqmP9gdwbpS0HfIG0qzle8mqWMfjQxCNBc34t8uEPQMg6ebE8zxCq0V9JeAOi13UI+ppau1FpcvWNxESiftBkcfidx/POMzIKXe0EqfY8k3tTE9tSa3RBDVLcbDiROIECgYAIzCFeoUQ9ZcEFtbvTgnvVUsNM8InGAOz7GIcGjN/atZHnxCml6LkO7aRvh2MGRPOmef6W2j4eETgdhAKo2/nfQLjU8KJHjEIfEzd6Gee5zYgStkfPsg5JCDrBjN0tF/vGmhGHKJbCAKm+G6SpkEuIR1gFjYYJIBq7RqMVxI6JsQ==";
    String jwtPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzvJDk9ZUqJXsUDsgyqgPZY86wgQDLWO1BX0i7oXv82m1OaKu+h282BS03H30ojvA8DtIKPLKyHZJdpolngLg/x7gRtXUcS4BwPxV3HsH2FsenbK3YVM+7JNNTk1Dxu4T4iNHfmDKhoaa3J/UMAhykuvfOnzwGh4stJYr08uHNpPuDhhslVMrEiOPxW/RWR+A8KJSMUbzqQpfpJzyen0J/zclHS8jvjXsIbS5jweL0DhR6IiU7n9wizClf11P/ZNUbU+dzC6YqEx9iKLi/zLXSJE+lY64vX2oq3u5G8GAwjb34WFVa0ZiePAnsNttJG7I/I7P34CZwGrVRExGQxZ65wIDAQAB";


    @Before
    public void setUp() throws Exception {
        // generate jwtService with a default exp time and private key
        blogenJwtService = new BlogenJwtService( 180000, jwtPrivateKey );
    }

    @Test
    public void should_generate_valid_token() throws Exception {
        int expMs = 180000;
        Instant issuedAt = Instant.now();
        String sub = "1";
        String[] scopes = {"API", "USER"};

        String jwt = blogenJwtService.generateToken(sub, Arrays.asList(scopes), issuedAt, expMs, jwtPrivateKey);

        assertThat( jwt, is( notNullValue() ));
        assertThat( verifyJwt(jwt), is(true) );
    }

    @Test
    public void should_generate_token_with_supplied_scopes() throws Exception {
        int expMs = 180000;
        Instant issuedAt = Instant.now();

        String sub = "1";
        String[] scopes = {"API", "USER"};

        String jwt = blogenJwtService.generateToken(sub, Arrays.asList(scopes), issuedAt, expMs, jwtPrivateKey);

        SignedJWT sJwt = SignedJWT.parse(jwt);
        final JWTClaimsSet claims = sJwt.getJWTClaimsSet();

        assertThat( claims.getSubject(), is( "1" ) );
        assertThat( claims.getClaims(), hasKey("scope"));
        List<String> scopeList = (List<String>) claims.getClaim("scope");
        assertThat( scopeList.size(), is(2) );
        assertThat( scopeList, contains("API", "USER") );
    }

    @Test
    public void should_always_generate_token_with_iat_and_exp_claims() throws Exception {
        int expMs = 180000;
        Instant issuedAt = LocalDateTime.of(1970, 2, 2, 2, 2).toInstant(ZoneOffset.UTC);
        Instant expectedExpiration = issuedAt.plusMillis( expMs );
        String[] scopes = {"API", "USER"};

        String jwt = blogenJwtService.generateToken("1", Arrays.asList(scopes), issuedAt, expMs, jwtPrivateKey);

        SignedJWT sJwt = SignedJWT.parse(jwt);
        final JWTClaimsSet claims = sJwt.getJWTClaimsSet();

        assertThat( claims.getClaims(), hasKey("iat"));
        assertThat( ((Date) claims.getClaim("iat")).toInstant(), is( issuedAt) );
        assertThat( claims.getClaims(), hasKey("exp"));
        assertThat( ((Date) claims.getClaim("exp")).toInstant(), is(expectedExpiration));
    }


    private boolean verifyJwt(String jwt ) throws Exception {
        // verify token signature
        SignedJWT sJWT = SignedJWT.parse( jwt );
        JWSVerifier verifier = new RSASSAVerifier( key(jwtPublicKey) );
        return sJWT.verify(verifier);
    }

    //generates the PublicKey used to verify the JWTs
    private RSAPublicKey key( String publicKeyStr ) throws Exception {
        byte[] bytes = Base64.getDecoder().decode( publicKeyStr.getBytes() );
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(bytes));
    }
}