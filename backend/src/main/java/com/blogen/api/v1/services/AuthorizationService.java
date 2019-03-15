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

    // username prefix for Blogen user that login via github
    String GITHUB_USER_PREFIX = "github_";

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

    /**
     * will attempt to login a Github Oauth2 user into Blogen. Checks for an existing Blogen user accounr
     * using a username of "github_{login}" where login is the login name of the github user. If no Blogen user is
     * found under this username, then a new Blogen user is created
     *
     * @param oAuth2User
     * @return a JWT token string (in compact claims form) giving the user access to the blogen API and website
     */
    String loginGithubUser(OAuth2User oAuth2User);
}
