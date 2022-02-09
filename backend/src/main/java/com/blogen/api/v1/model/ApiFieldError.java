package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "the name of the request field or parameter where an error occured", accessMode = Schema.AccessMode.READ_ONLY, example = "password")
    private String field;
    @Schema(description = "the string describing the error", accessMode = Schema.AccessMode.READ_ONLY, example = "password must be more than 8 characters")
    private String message;
    @Schema(description = "the value of the field that was erroneous", accessMode = Schema.AccessMode.READ_ONLY)
    private Object rejectedValue;

}
