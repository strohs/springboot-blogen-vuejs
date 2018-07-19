package com.blogen.api.v1.services;

import com.blogen.api.v1.model.JwtAuthenticationResponse;
import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;

/**
 * Service for new user sign-up and user log-ins
 * Author: Cliff
 */
public interface AuthorizationService {

    /**
     * signup a new user
     * @param userDTO
     * @return userDTO containing the data that was saved to the DB
     */
    UserDTO signUpUser( UserDTO userDTO );

    /**
     * login a user using their username and password
     * @param loginRequestDTO
     * @return a String representation of the authenticated user's JSON Web Token
     */
    JwtAuthenticationResponse authenticateAndLoginUser( LoginRequestDTO loginRequestDTO );
}
