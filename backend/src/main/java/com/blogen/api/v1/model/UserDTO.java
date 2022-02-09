package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @Schema(description = "User ID", accessMode = Schema.AccessMode.READ_ONLY, example = "25")
    private Long id;

    @Schema(description = "first name of the user", example = "Mary")
    private String firstName;

    @Schema(description = "last name of the user", example = "Jones")
    private String lastName;

    @Schema(description = "user name of the user", example = "superCool2049")
    private String userName;

    @Schema(description = "users email address", example = "crazy@gmail.com")
    private String email;

    @Schema(description = "users password", example = "superSecretPassword")
    private String password = "";

    @Schema(description = "users avatar image file name", example = "avatar43.jpg")
    private String avatarImage;

    @Schema(description = "the security privileges assigned to the user", accessMode = Schema.AccessMode.READ_ONLY, example = "ROLE_USER,ROLE_ADMIN")
    private List<String> roles;

    @Schema(description = "url that identifies this user", accessMode = Schema.AccessMode.READ_ONLY, example = "/api/v1/users/23")
    private String userUrl;

}
