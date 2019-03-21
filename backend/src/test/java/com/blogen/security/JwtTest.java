package com.blogen.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


public class JwtTest {

    private Long id = 434L;
    private Long jwtExpirationInMs = 180000L;
    private String jwtSecret = "jwtsecretpasswordthatmustbeatleasttwohundredandfiftysixbits";

    @Test
    @Ignore
    public void testJwtCreation() {
        Date now = new Date();
        Date expiryDate = new Date( now.getTime() + jwtExpirationInMs );

        try {
            JWSSigner signer = new MACSigner( jwtSecret );
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject( Long.toString( id) )
                    .issueTime( now )
                    .expirationTime(expiryDate)
                    .build();
            SignedJWT signedJWT = new SignedJWT( new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            String s = signedJWT.serialize();
            assertNotNull( s );

            // test parsing
            SignedJWT sJWT = SignedJWT.parse(s);
            JWSVerifier verifier = new MACVerifier(jwtSecret);
            final boolean verified = sJWT.verify(verifier);
            assert( verified );

            final JWTClaimsSet claims = sJWT.getJWTClaimsSet();
            assertThat( claims.getSubject(), is( "434" ) );
        } catch (JOSEException e) {
            throw new IllegalStateException("could not generate JWT: " + e.getMessage() );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
