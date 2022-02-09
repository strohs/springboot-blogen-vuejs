package com.blogen.api.v1.model;

import lombok.Data;

import java.util.List;

/**
 * A response object sent to users after they successfully log in via the REST Api.
 * It contains their access token (i.e. their JWT) and their current user data
 * Author: Cliff
 */
@Data
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private UserDTO user;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponse(String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
