package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "a list of all avatar file names", accessMode = Schema.AccessMode.READ_ONLY)
    List<String> avatars;
}
