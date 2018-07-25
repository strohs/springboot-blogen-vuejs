package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostUserDTO;
import com.blogen.api.v1.services.UserService;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;

/**
 * MapStruct mappers for mapping data between {@link com.blogen.domain.Post} and {@link com.blogen.api.v1.model.PostDTO}
 *
 * @author Cliff
 */
@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    //@Mapping( source = "user.userName", target = "userName")
    //@Mapping( target = "created", dateFormat = "EEE MMM dd, yyyy hh:mm a")
    PostDTO postToPostDto( Post post );

    //@Mapping( target = "user.userName", source = "userName")
    Post postDtoToPost( PostDTO postDTO );

    @Mapping( target = "categoryUrl", expression = "java(com.blogen.api.v1.services.CategoryService.buildCategoryUrl(category))")
    CategoryDTO categoryToCategoryDto( Category category );
    Category categoryDtoToCategory( CategoryDTO categoryDTO );

    default PostUserDTO userToPostUserDto( User user ) {
        PostUserDTO postUser = new PostUserDTO();
        postUser.setId( user.getId() );
        postUser.setUserName( user.getUserName() );
        postUser.setUserUrl( UserService.buildUserUrl( user ) );
        postUser.setAvatarUrl( user.getUserPrefs().getAvatarImage() );
        return postUser;
    }

    User postUserDtoToUser( PostUserDTO postUserDTO );

    //@Mapping( target = "user.userName", source = "userName")
    //null fields in postDTO will set corresponding Post fields to null
    //Post updatePostFromDTO( PostDTO postDTO, @MappingTarget Post post );



}
