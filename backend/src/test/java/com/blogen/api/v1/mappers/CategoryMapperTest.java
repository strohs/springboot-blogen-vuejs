package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Unit Test data transfer between Category/CategoryDTO
 *
 * @author Cliff
 */
public class CategoryMapperTest {

    private final static String NAME = "Business";
    private final static Long ID = 1L;

    private CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void categoryToCategoryDto() {
        //given
        Category category = new Category();
        category.setId( ID );
        category.setName( NAME );

        //when
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDto( category );

        //then
        assertThat( categoryDTO.getId(), equalTo( ID ) );
        assertThat( categoryDTO.getName(), equalTo( NAME ) );

    }

    @Test
    public void categoryDtoToCategory() {
        //given
        CategoryDTO categoryDTO = new CategoryDTO( 1L, NAME, null );

        //when
        Category category = categoryMapper.categoryDtoToCategory( categoryDTO );

        //then
        assertThat( category.getId(), equalTo( ID ) );
        assertThat( category.getName(), equalTo( NAME ) );
    }
}