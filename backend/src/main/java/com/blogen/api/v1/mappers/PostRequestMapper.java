package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.PostRequestDTO;
import com.blogen.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mappers for mapping data between {@link Post} and {@link PostRequestDTO}
 *
 * @author Cliff
 */
//source properties that are null should not be mapped onto target properties
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PostRequestMapper {

    PostRequestMapper INSTANCE = Mappers.getMapper( PostRequestMapper.class );

    @Mapping( target = "categoryId", source = "category.id")
    PostRequestDTO postToPostRequestDto( Post post );

    @Mapping( target = "category.id", source = "categoryId")
    Post postRequestDtoToPost( PostRequestDTO postDTO );

    void updatePostFromPostRequestDTO( PostRequestDTO requestDTO, @MappingTarget Post post );




}
