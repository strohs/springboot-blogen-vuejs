package com.blogen.api.v1.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Used to transfer username and password when a user logs in
 * 
 * Author: Cliff
 */
@Data
@Builder
public class LoginRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;


}
