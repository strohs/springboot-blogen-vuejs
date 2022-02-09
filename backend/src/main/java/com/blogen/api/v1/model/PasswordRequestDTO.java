package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * request DTO for user passwords
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDTO {

    @Schema(description = "users password", example = "superSecretPassword")
    private String password = "";
}
