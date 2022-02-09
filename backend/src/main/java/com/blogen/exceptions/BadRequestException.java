package com.blogen.exceptions;

/**
 * General exception class for resources requests that were malformed
 *
 * @author Cliff
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException( String message) {
        super(message);
    }

    public BadRequestException( String message, Throwable cause) {
        super(message, cause);
    }
}
