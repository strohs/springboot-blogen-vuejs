package com.blogen.exceptions;

/**
 * General exception class for resources that could not be found (typically something was not found in the DB).
 *
 * @author Cliff
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException( String message) {
        super(message);
    }

    public NotFoundException( String message, Throwable cause) {
        super(message, cause);
    }
}
