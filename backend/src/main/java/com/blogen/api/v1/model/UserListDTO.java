package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {

    @Schema(description = "list container for userDTO", accessMode = Schema.AccessMode.READ_ONLY)
    private List<UserDTO> users;

}
