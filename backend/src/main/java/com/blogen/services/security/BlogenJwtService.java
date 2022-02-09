package com.blogen.services.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * creates JSON Web Tokens for use by Blogen. The tokens are signed using a RS256.
 *
 * These tokens will typically have the subject set to the user's ID within the Blogen database, and will have
 * a scope set to "api" which will grant them access to the blogen REST API.
 *
 * You may create a token directly by calling the generateToken() method and supplying the required method parameters.
 * If not specified, the expirationMs will default to the configured value in the applications.properties: defaultExpirationSecs
 */
@Slf4j
@Service
public class BlogenJwtService implements JwtService {

    private final int defaultExpirationSecs;
    private final JwtEncoder jwtEncoder;

    @Autowired
    public BlogenJwtService(@Value("${app.jwtExpirationSec}") int defaultExpirationSecs,
                            JwtEncoder jwtEncoder) {
        this.defaultExpirationSecs = defaultExpirationSecs;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(String subject,
                                List<BlogenAuthority> scopes,
                                Instant issueTime,
                                Long expirationSecs) {
        Instant issuedAt = issueTime == null ? Instant.now() : issueTime;
        long expSecs = expirationSecs == null ? defaultExpirationSecs : expirationSecs;
        Instant expiry = issuedAt.plusSeconds(expSecs);

        // build the scopes as a space separated string of authorities
        String scope = scopes.stream().map(Enum::toString).collect(Collectors.joining(" "));

        // build the claims to be included in the JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(issuedAt)
                .expiresAt(expiry)
                .subject(subject)
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }



}
