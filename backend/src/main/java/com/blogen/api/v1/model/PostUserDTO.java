package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object containing {@link com.blogen.domain.User} id and username of the user that made the post
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUserDTO {

    @Schema(description = "User ID", accessMode = Schema.AccessMode.READ_ONLY, example = "25")
    private Long id;

    @Schema(description = "user name", accessMode = Schema.AccessMode.READ_ONLY, example = "superCool2049")
    private String userName;

    @Schema(description = "relative url to the users avatar image", accessMode = Schema.AccessMode.READ_ONLY, example = "/avatars/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "url that identifies this user", accessMode = Schema.AccessMode.READ_ONLY, example = "/api/v1/users/23")
    private String userUrl;
}
