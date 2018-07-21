package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.PostUserDTO;
import com.blogen.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Utilizes MapStruct to map between Blogen {@link User} and {@link PostUserDTO}
 *
 * @author Cliff
 */
@Mapper
public interface PostUserMapper {

    PostUserMapper INSTANCE = Mappers.getMapper( PostUserMapper.class );

    @Mapping( target = "userUrl", expression = "java(com.blogen.api.v1.services.UserService.buildUserUrl(user))")
    PostUserDTO userToPostUserDto( User user );

    User postUserDtoToUser( PostUserDTO postUserDTO );

}
