package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.domain.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Utilizes MapStruct to map between Blogen {@link com.blogen.domain.Category} and {@link com.blogen.api.v1.model.CategoryDTO}
 *
 * @author Cliff
 */
@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper( CategoryMapper.class );

    CategoryDTO categoryToCategoryDto( Category category );

    Category categoryDtoToCategory( CategoryDTO categoryDTO );

}
