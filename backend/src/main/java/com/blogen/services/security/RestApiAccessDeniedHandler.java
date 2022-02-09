package com.blogen.services.security;

import com.blogen.api.v1.model.ApiErrorsView;
import com.blogen.api.v1.model.ApiGlobalError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A custome AccessDeniedHandler for the REST Api that returns an
 * ApiErrorsView object as JSON, along with a status code of 403
 * This Handler is invoked by Spring Security if a user's JWT doesn't have
 * the correct credentials to access a resource
 */
public class RestApiAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        // use our ApiErrorsView to log the details of the error
        ApiGlobalError globalError = new ApiGlobalError(accessDeniedException.getMessage());
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
