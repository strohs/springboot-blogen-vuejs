package com.blogen.api.v1.model;

import lombok.Data;

/**
 * Author: Cliff
 */
@Data
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
