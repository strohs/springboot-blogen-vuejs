package com.blogen.api.v1.model;

import lombok.Data;

import java.util.List;

/**
 * Author: Cliff
 */
@Data
public class AuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    // the users role at Blogen, either: 'USER' or 'ADMIN'
    private UserDTO user;

    public AuthenticationResponse( String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthenticationResponse( String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
