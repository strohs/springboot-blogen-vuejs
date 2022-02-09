package com.blogen.api.v1.services.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserLoginService {

    /**
     * login a user from an OAuth2 provider into Blogen, possibly creating a Blogen User for them and finally returning
     * a valid JWT token.
     *
     * Implementors of this interface may need to create a new Blogen user for the OAuth2 user if this is the
     * first time logging in via OAuth2. Additionally, there will need to be a way to map user details from OAuth2
     * (possibly openID) to a Blogen User domain object
     *
     * @param provider
     * @param oAuth2User
     * @return a valid Blogen JWT string (base64 encoded)
     */
    String loginUser(OAuth2Providers provider, OAuth2User oAuth2User);

}
