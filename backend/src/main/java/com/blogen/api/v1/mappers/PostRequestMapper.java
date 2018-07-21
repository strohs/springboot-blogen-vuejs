package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.PostRequestDTO;
import com.blogen.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mappers for mapping data between {@link Post} and {@link PostRequestDTO}
 *
 * @author Cliff
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PostRequestMapper {
    //source properties that are null should not be mapped onto target properties

    PostRequestMapper INSTANCE = Mappers.getMapper( PostRequestMapper.class );


    PostRequestDTO postToPostRequestDto( Post post );

    Post postRequestDtoToPost( PostRequestDTO postDTO );

    void updatePostFromPostRequestDTO( PostRequestDTO requestDTO, @MappingTarget Post post );




}
