package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This is a "data" class that contains details of any errors that occurred while working with the REST API.
 * This class (and it's helper classes: APIFieldError, ApiGlobalError> will be converted to JSON and returned in the
 * HTTP Response should any errors occur
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorsView {

    private List<ApiFieldError> fieldError;
    private List<ApiGlobalError> globalError;

}
