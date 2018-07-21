package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

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

    //@Mapping( target = "user.userName", source = "userName")
    //null fields in postDTO will set corresponding Post fields to null
    //Post updatePostFromDTO( PostDTO postDTO, @MappingTarget Post post );



}
