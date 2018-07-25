package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @ApiModelProperty( value = "User ID", readOnly = true, example = "25")
    private Long id;

    @ApiModelProperty(value = "user name", readOnly = true, example = "superCool2049")
    private String userName;

    @ApiModelProperty(value = "relative url to the users avatar image", readOnly = true, example = "/avatars/avatar.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "url that identifies this user", readOnly = true, example = "/api/v1/users/23")
    private String userUrl;
}
