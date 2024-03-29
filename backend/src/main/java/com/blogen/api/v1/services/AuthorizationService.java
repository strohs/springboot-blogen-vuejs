package com.blogen.api.v1.services;

import com.blogen.api.v1.model.LoginResponse;
import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Service for new user sign-up and user log-ins via the Blogen sign-up page or via OAuth2 providers (github and google)
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
     * login a user using their username and password, and if authenticated, generate a JSON Web Token
     * for them. The JSON Web Token must then be sent in the header, when the user tries to access a protected
     * resource (mainly the REST API)
     * @param loginRequestDTO
     * @return a String representation of the authenticated user's JSON Web Token
     */
    LoginResponse authenticateAndLoginUser(LoginRequestDTO loginRequestDTO );


    String authenticateAndLoginUser(String username, String password);

    /**
     * Checks if the given userName exists in our User table
     * @param userName the userName to search for
     * @return true if the username exists, else false
     */
    Boolean userNameExists( String userName );

}
