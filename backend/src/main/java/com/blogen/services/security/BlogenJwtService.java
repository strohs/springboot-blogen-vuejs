package com.blogen.services.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * creates JSON Web Tokens for use by Blogen. The tokens are signed using a RSA256.
 *
 * These tokens will typically have the subject set to the user's ID within the Blogen database, and will have
 * a scope set to "api" which will grant them access to the blogen REST API.
 *
 * You may create a token directly by calling the generateToken() method and supplying the required method parameters,
 * OR use the BlogenJetService.Builder. The builder requires, at a minimum, a subject and a list of scopes. The
 * privateKey and expirationMs will default to values configured in the applications.properties
 */
@Slf4j
@Service
public class BlogenJwtService implements JwtService {

    private int defaultExpirationMs;
    private String defaultPrivateKeyStr;
    private PrivateKey defaultPrivateKey;

    public class Builder {
        private String subject;
        private List<String> scopes;
        private Instant issuedAt;
        private int expirationMs;
        private String privateKey;

        Builder(String subject, List<String> scopes, Instant issuedAt, int expirationMs, String privateKey) {
            this.subject = subject;
            this.scopes = scopes;
            this.expirationMs = expirationMs;
            this.issuedAt = issuedAt;
            this.privateKey = privateKey;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withScopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder withExpirationMs(int expirationMs) {
            this.expirationMs = expirationMs;
            return this;
        }

        public Builder withIssuedAt(Instant issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder withPrivateKey(String privateKeyStr) {
            this.privateKey = privateKeyStr;
            return this;
        }

        public String buildToken() {
            return generateToken(subject, scopes, issuedAt, expirationMs, privateKey);
        }
    }

    public Builder builder() {
        return new Builder("subject", Collections.emptyList(), Instant.now(), defaultExpirationMs, defaultPrivateKeyStr);
    }

    public BlogenJwtService(@Value("${app.jwtExpirationMs}") int defaultExpirationMs,
                            @Value("${app.jwtPrivateKey}") String defaultPrivateKeyStr) {
        this.defaultPrivateKeyStr = defaultPrivateKeyStr;
        this.defaultPrivateKey = decodeKey(defaultPrivateKeyStr);
        this.defaultExpirationMs = defaultExpirationMs;
    }

    @Override
    public String generateToken(String subject,
                                List<String> scopes,
                                Instant issueTime,
                                int expirationMs,
                                String privateKey) {
        Date issuedAt = issueTime == null ? new Date() : Date.from( issueTime );
        Date expiryDate = new Date( issuedAt.getTime() + expirationMs);

        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
            JWSSigner signer = new RSASSASigner( decodeKey(privateKey) );
            JSONArray scopesArr = new JSONArray();
            scopesArr.addAll(scopes);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject( subject )
                    .claim("scope", scopesArr )
                    .issueTime(issuedAt)
                    .expirationTime( expiryDate )
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
    private static PrivateKey decodeKey(String encodedKey) {
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
