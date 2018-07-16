package com.blogen.services.security;

import com.blogen.domain.User;
import com.blogen.repositories.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * generates a JWT after a user logs in successfully, and validates the JWT sent in the Authorization header of
 * the requests
 * <p>
 * Author: Cliff
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private UserRepository userRepository;

    @Autowired
    public JwtTokenProvider( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    public String generateToken( Authentication authentication ) {
        //TODO check this and make sure UserDetails is actually returned
        UserDetails userPrincipal = ( UserDetails ) authentication.getPrincipal();
        //get user details
        User user = userRepository.findByUserName( userPrincipal.getUsername() );

        Date now = new Date();
        Date expiryDate = new Date( now.getTime() + jwtExpirationInMs );

        return Jwts.builder()
                .setSubject( Long.toString( user.getId() ) )
                .setIssuedAt( new Date() )
                .setExpiration( expiryDate )
                .signWith( SignatureAlgorithm.HS512, jwtSecret )
                .compact();
    }

    Long getUserIdFromJWT( String token ) {
        Claims claims = Jwts.parser()
                .setSigningKey( jwtSecret )
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong( claims.getSubject() );
    }

    boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
