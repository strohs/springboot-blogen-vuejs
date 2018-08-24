package com.blogen.services.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This method is called whenever an exception is thrown due to an unauthenticated user trying to access a
 * resource that requires authentication. We’ll simply respond with a 401 error saying that the user is not
 * authorized to access the resource.
 *
 * Author: Cliff
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence( HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse,
                          AuthenticationException e ) throws IOException, ServletException {
        log.error("Unauthorized error. JWT is missing or expired. Message {}", e.getMessage());
        httpServletResponse.sendError( HttpServletResponse.SC_UNAUTHORIZED,
                "You're not authorized to access this resource. Your authorization token is missing or expired");
    }
}
