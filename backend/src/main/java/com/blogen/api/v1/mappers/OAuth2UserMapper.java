package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.OAuth2Provider;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * maps Oauth2User attributes into a Blogen UserDTO object. Special care has to be taken to provide default
 * values for fields that Oauth2 may not provide to us.
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OAuth2UserMapper {

    OAuth2UserMapper INSTANCE = Mappers.getMapper( OAuth2UserMapper.class );

    /**
     * maps github oauth2 user information to a blogen UserDTO object
     * @param oath2User
     * @return
     */
    default UserDTO githubOAuth2UserToUserDTO(OAuth2User oath2User){
        //   avatar_uri = "https://......"
        //
        UserDTO user = new UserDTO();
        user.setUserName( OAuth2Provider.GITHUB.toString().toLowerCase() + oath2User.getName() );
        user.setFirstName( "anonymous" );
        user.setLastName( "anonymous" );
        user.setEmail( oath2User.getAttributes().getOrDefault("email", "github@example.com").toString() );
        // hardcode pw for now, ideally force them to change it on blogen profiles page
        user.setPassword("github");

        return user;
    }

    /**
     * maps google oauth2 user information to a blogen UserDTO object
     * @param oath2User
     * @return
     */
    default UserDTO googleOAuth2UserToUserDTO(OAuth2User oath2User){

        UserDTO user = new UserDTO();
        user.setUserName( OAuth2Provider.GOOGLE.toString().toLowerCase() + oath2User.getName() );
        user.setFirstName( oath2User.getAttributes().get("given_name").toString() );
        user.setLastName( oath2User.getAttributes().get("family_name").toString() );
        user.setEmail( oath2User.getAttributes().getOrDefault("email", "google@example.com").toString() );
        // hardcode password for now
        user.setPassword("goggle");

        return user;
    }

}
