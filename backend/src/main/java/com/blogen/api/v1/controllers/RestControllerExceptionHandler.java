package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.ApiErrorsView;
import com.blogen.api.v1.model.ApiFieldError;
import com.blogen.api.v1.model.ApiGlobalError;
import com.blogen.exceptions.BadRequestException;
import com.blogen.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Exception Handlers for REST Controllers
 *
 * @author Cliff
 */
@Slf4j
@ControllerAdvice("com.blogen.api.v1.controllers")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( {NotFoundException.class} )
    public ResponseEntity<Object> handleNotFoundException( Exception exception, WebRequest request ){
        log.error( exception.getMessage() );
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler( {BadRequestException.class} )
    public ResponseEntity<Object> handleBadRequestException( Exception exception, WebRequest request ) {
        log.error( exception.getMessage() );
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.BAD_REQUEST );
    }

    //return a 401 code when a user provides invalid login credentials are token
    @ExceptionHandler( {BadCredentialsException.class} )
    public ResponseEntity<Object> handleBadCredentialsException( Exception exception, WebRequest request ) {
        log.error( exception.getMessage() );
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.UNAUTHORIZED );
    }

    @ExceptionHandler( {DataIntegrityViolationException.class} )
    public ResponseEntity<Object> handleRowExists( Exception exception, WebRequest request ) {
        log.error( exception.getMessage() );
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY );
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch( TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request ) {
        log.error( ex.getMessage() );
        String methodParamName = ( ex instanceof MethodArgumentTypeMismatchException) ? (( MethodArgumentTypeMismatchException ) ex).getName() : "";
        List<ApiFieldError> apiFieldErrors = new ArrayList<>();
        List<ApiGlobalError> apiGlobalErrors = new ArrayList<>();
        ApiGlobalError globalError = new ApiGlobalError();
        globalError.setMessage( "invalid type sent for parameter: " + methodParamName + "  with value:" + ex.getValue() );
        apiGlobalErrors.add( globalError );

        ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);

        return new ResponseEntity<>(apiErrorsView, HttpStatus.BAD_REQUEST);
    }

    //TODO WebBinder errors are caught here, may need to return different HttpStatus
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException exception,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request ) {
        log.error( exception.getMessage() );
        BindingResult bindingResult = exception.getBindingResult();

        List<ApiFieldError> apiFieldErrors = bindingResult
                .getFieldErrors()
                .stream()
                .map( fieldError ->
                        new ApiFieldError( fieldError.getField(), fieldError.getDefaultMessage(),fieldError.getRejectedValue() ) )
                .collect( toList());

        List<ApiGlobalError> apiGlobalErrors = bindingResult
                .getGlobalErrors()
                .stream()
                .map(globalError -> new ApiGlobalError(
                        globalError.getCode())
                )
                .collect( toList() );

        ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);

        return new ResponseEntity<>(apiErrorsView, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions( Exception exception ) {
        log.error( exception.getMessage() );
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
