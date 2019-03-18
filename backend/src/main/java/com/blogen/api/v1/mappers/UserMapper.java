package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.AuthorizationService;
import com.blogen.api.v1.services.UserService;
import com.blogen.domain.Role;
import com.blogen.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.List;

/**
 * MapStruct mappers for mapping between {@link com.blogen.domain.User} and {@link com.blogen.api.v1.model.UserDTO}
 *
 * @author Cliff
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    //NullValueCheckStrategy.ALWAYS ensures that source properties that are NULL, don't get mapped
    // onto target properties

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping( target = "avatarImage", source = "userPrefs.avatar.fileName")
    @Mapping( target = "userUrl", expression = "java(com.blogen.api.v1.services.UserService.buildUserUrl(user))")
    @Mapping( target = "password", ignore = true)
    UserDTO userToUserDto( User user );

    @Mapping( target = "userPrefs.avatar.fileName", source = "avatarImage")
    User userDtoToUser( UserDTO userDTO );
    
    @Mapping( source = "avatarImage", target = "userPrefs.avatar.fileName")
    void updateUserFromDTO( UserDTO userDTO, @MappingTarget User user );

    /**
     * maps github oauth2 user information to a blogen UserDTO object
     * @param oath2User
     * @return
     */
    default UserDTO githubOAuth2UserToUser(OAuth2User oath2User){
        //   avatar_uri = "https://......"
        //
        UserDTO user = new UserDTO();
        user.setUserName( AuthorizationService.GITHUB_USER_PREFIX + oath2User.getAttributes().get("login"));
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
    default UserDTO googleOAuth2UserToUser(OAuth2User oath2User){

        UserDTO user = new UserDTO();
        user.setUserName( AuthorizationService.GOOGLE_USER_PREFIX + oath2User.getAttributes().get("sub"));
        user.setFirstName( oath2User.getAttributes().get("given_name").toString() );
        user.setLastName( oath2User.getAttributes().get("family_name").toString() );
        user.setEmail( oath2User.getAttributes().getOrDefault("email", "google@example.com").toString() );
        // hardcode password for now
        user.setPassword("goggle");

        return user;
    }

    default List<String> asStrings( List<Role> roles) {
        // intentionally initialized to null
        List<String> strings = null;
        if ( roles != null ) {
            strings = new ArrayList<>();
            for (Role role : roles) {
                strings.add( role.getRole() );
            }
        }
        return strings;
    }

    default List<Role> asRoles(List<String> strings) {
        // set roles to empty list so that MapStruct does not automatically map it to null
        List<Role> roles = new ArrayList<>();
        if ( strings != null ) {
            strings.forEach( s -> {
                Role r = new Role();
                r.setRole( s );
            });
        }
        return roles;
    }
}
