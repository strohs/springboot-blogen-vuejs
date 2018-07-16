package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFieldError {

    private String field;
    private String message;
    private Object rejectedValue;

}
