package com.blogen.services.security;

import com.blogen.domain.User;

import java.time.Instant;
import java.util.List;

public interface JwtService {

    /**
     * generates a signed Json Web Token aka (JWT)
     * @param subject - the string to put into the "sub" claim of the JWT
     * @param scopes - the scopes to add to the "scope" claim of the JWT
     * @param expirationMs - expiration time of the JWT in milliseconds
     * @param privateKey - the key to use for signing the JWT
     * @return - a String containing the generated JWT in compact claims format
     */
    String generateToken(String subject,
                         List<String> scopes,
                         Instant issuedAt,
                         int expirationMs,
                         String privateKey);

}
