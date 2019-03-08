package com.blogen.services.security;

import java.util.List;

public interface JwtTokenProvider {

    /**
     * generate a JWT in compact claims form
     * @param subject - the subject to put into the JWT's "subject" field
     * @param scopes - a list of scopes to put into the JWT's scopes field
     * @return a JWT in compact form (BASE64 encoded)
     */
    String generateToken(String subject, List<String> scopes);
}
