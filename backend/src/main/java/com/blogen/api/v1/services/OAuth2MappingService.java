package com.blogen.api.v1.services;

import com.blogen.api.v1.model.UserDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2MappingService {

    /**
     * logs a OAuth2 user into Blogen, creating a new account for them if that haven't already logged in before,
     * and returning a Jwt string that will grant them access to Blogen website and its REST API
     *
     * @param oAuth2User - contains the OAuth2 user information used to populate a blogen User domain object.
     *                   at a minimum, there must be a unique field that can be used to generate a blogen
     *                   User.username.
     * @return a UserDTO containing user information from the OAuth2User
     */
    UserDTO mapUser(OAuth2Provider provider, OAuth2User oAuth2User);



}
