package com.blogen.services.security;


import java.time.Instant;
import java.util.List;

public interface JwtService {

    /**
     * generates a signed Json Web Token (JWT)
     * @param subject - the string to put into the "sub" claim of the JWT, for this example project
     *                the subject will be the end-user's unique ID within the blogen Users table
     * @param scopes - the scopes to add to the "scope" claim of the JWT, these will be added as a
     *               space separated list of strings
     * @param expirationSecs - expiration time of the JWT in seconds. If this is null, it will default
     *                       to the defaultExpirationSecs configured within application.properties
     * @param issuedAt - the issued at time to use in the "iat" field of the JWT. If this is null it
     *                 will default to Instant.now()
     * @return - a Base64 encoded String containing the generated JWT in compact claims format
     */
    String generateToken(String subject,
                         List<BlogenAuthority> scopes,
                         Instant issuedAt,
                         Long expirationSecs);

}
