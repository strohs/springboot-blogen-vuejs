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
public class ApiGlobalError {

    @Schema(description = "a 'global' error message string", accessMode = Schema.AccessMode.READ_ONLY, example = "servers are down")
    private String message;
}
