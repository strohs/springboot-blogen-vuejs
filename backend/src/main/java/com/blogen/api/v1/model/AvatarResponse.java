package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for sending a list of avatar names
 * Author: Cliff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvatarResponse {

    @ApiModelProperty(value = "a list of avatar file names", readOnly = true)
    List<String> avatars;
}
