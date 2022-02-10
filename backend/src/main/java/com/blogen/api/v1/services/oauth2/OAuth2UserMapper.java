package com.blogen.api.v1.services.oauth2;

import com.blogen.api.v1.model.UserDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * maps user information (details) provided by OAuth2 into a Blogen UserDTO object
 */
public interface OAuth2UserMapper {

    /**
     * map OAuth2 user information into a UserDTO. OAuth2 fields that aren't provided will be set to "anonymous"
     * At a minimum, the UserDTO.username field must be set to a unique value to avoid collisions in the Blogen
     * database. Therefore; GitHub users will be assigned their GitHub "id" with the suffix "@GitHub" appended.
     * Google users will be assigned: TODO
     *
     * @param oAuth2User - contains the OAuth2 user information used to populate a blogen User domain object.
     *                   at a minimum, there must be a unique field that can be used to generate a blogen
     *                   User.username.
     * @return a UserDTO containing user information from the OAuth2User
     */
    UserDTO mapUser(OAuth2User oAuth2User);

    static OAuth2UserMapper getUserMapperForProvider(OAuth2Providers provider) throws IllegalArgumentException {
        switch (provider) {
            case GITHUB: return OAuth2UserMapper.mapGithubUser();
            case GOOGLE: return OAuth2UserMapper.mapGoogleUser();
            default: throw new IllegalArgumentException("unknown OAuth2 mapping provider: " + provider.toString() );
        }
    }
    static OAuth2UserMapper mapGithubUser() {
        return oAuth2User -> {
            UserDTO user = new UserDTO();
            user.setUserName( oAuth2User.getName() + "@GitHub");
            user.setFirstName( "anonymous" );
            user.setLastName( "anonymous" );
            user.setEmail( oAuth2User.getAttributes().getOrDefault("email", "github@example.com").toString() );
            // hardcode pw for now, ideally force them to change it on blogen profiles page
            user.setPassword("github");

            return user;
        };
    }

    static OAuth2UserMapper mapGoogleUser() {
        return oAuth2User -> {
            UserDTO user = new UserDTO();
            user.setUserName( oAuth2User.getName() + "@Google");
            user.setFirstName( oAuth2User.getAttributes().get("given_name").toString() );
            user.setLastName( oAuth2User.getAttributes().get("family_name").toString() );
            user.setEmail( oAuth2User.getAttributes().getOrDefault("email", "google@example.com").toString() );
            // hardcode password for now
            user.setPassword("goggle");

            return user;
        };
    }

}
