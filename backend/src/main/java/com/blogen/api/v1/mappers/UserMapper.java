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
