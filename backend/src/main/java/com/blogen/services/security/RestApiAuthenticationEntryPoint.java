package com.blogen.services.security;

import com.blogen.api.v1.model.ApiErrorsView;
import com.blogen.api.v1.model.ApiGlobalError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A Custom AuthenticationEntryPoint that returns a ApiErrorsView object as serialized JSON.
 * This entry point is triggered if a user tries to access a secured REST Api endpoint but
 * doesn't provide their JWT or their JWT is malformed
 */
public class RestApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Jackson object mapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED; // 401

        // use our ApiErrorsView to log the details of the error
        ApiGlobalError globalError = new ApiGlobalError(authException.getMessage());
        List<ApiGlobalError> globalErrors = List.of(globalError);
        ApiErrorsView errorsView = new ApiErrorsView(null, globalErrors);

        // setting the response HTTP status code
        response.setStatus(httpStatus.value());

        // serializing the response body in JSON
        response
                .getOutputStream()
                .println(objectMapper.writeValueAsString(errorsView));
    }
}
