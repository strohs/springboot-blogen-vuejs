package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * request DTO for user passwords
 * @author Cliff
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDTO {

    @ApiModelProperty(value = "users password", example="superSecretPassword")
    private String password = "";
}
