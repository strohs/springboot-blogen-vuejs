package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.domain.Category;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Utilizes MapStruct to map between Blogen {@link com.blogen.domain.Category} and
 * {@link com.blogen.api.v1.model.CategoryDTO}
 *
 * @author Cliff
 */
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper( CategoryMapper.class );

    @Mapping( target = "categoryUrl", expression = "java(com.blogen.api.v1.services.CategoryService.buildCategoryUrl(category))")
    CategoryDTO categoryToCategoryDto( Category category );

    Category categoryDtoToCategory( CategoryDTO categoryDTO );

    void updateCategoryFromCategoryDTO( CategoryDTO requestDTO, @MappingTarget Category category );
}
