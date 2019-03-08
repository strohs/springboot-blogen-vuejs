package com.blogen.services.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * creates JSON Web Tokens for use by Blogen. The tokens are signed using RSA256.
 *
 * These tokens will typically have the subject set to the user's ID within the Blogen database, and will have
 * a scope set to "api" which will grant them access to the blogen REST API.
 */
@Slf4j
@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    // the tokens expiration time in milliseconds
    private int jwtExpirationMs;
    private PrivateKey privateKey;


    public JwtTokenProviderImpl(@Value("${app.jwtExpirationMs}") int jwtExpirationMs,
                                @Value("${app.jwtPrivateKey}") String privateKeyStr) {
        this.privateKey = decodeKey(privateKeyStr);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    public String generateToken( String subject, List<String> scopes) {
        Date now = new Date();
        Date expiryDate = new Date( now.getTime() + jwtExpirationMs);

        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
            JWSSigner signer = new RSASSASigner(privateKey);
            JSONArray scopesArr = new JSONArray();
            scopesArr.addAll(scopes);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject( subject )
                    .claim("scope", scopesArr )
                    .issueTime( now )
                    .expirationTime(expiryDate)
                    .build();
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("could not generate JWT: {}", e.getMessage());
            throw new IllegalStateException("could not generate JWT: " + e.getMessage() );
        }
    }

    /**
     * decodes a BASE64 RSA private key into a java.security.PrivateKey
     * @param encodedKey - base64 encoded private key string
     * @return - java.security.PrivateKey
     */
    private PrivateKey decodeKey(String encodedKey) {
        byte[] bytes = Base64.getDecoder().decode(encodedKey.getBytes());
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(ks);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("could not decode private RSA key: {}", e.getMessage());
            throw new IllegalStateException("could not generate private RSA key");
        }

    }
}
