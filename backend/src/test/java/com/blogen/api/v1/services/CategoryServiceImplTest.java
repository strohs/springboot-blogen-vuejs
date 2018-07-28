package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.CategoryController;
import com.blogen.api.v1.mappers.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.Category;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.services.utils.PageRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * Unit Tests for the Category REST Service
 * @author Cliff
 */
public class CategoryServiceImplTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PageRequestBuilder pageRequestBuilder;

    private CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    private Category cat_1;
    private Category cat_2;
    private Category newCat;
    private CategoryDTO newCatDTO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        categoryService = new CategoryServiceImpl( categoryRepository, categoryMapper, pageRequestBuilder );
        cat_1 = Builder.buildCategory( 1L, "Business");
        cat_2 = Builder.buildCategory( 2L,"Health & Wellness" );
        newCat = Builder.buildCategory( 3L, "New Category" );
        newCatDTO = new CategoryDTO( 3L,"New Category", null );
    }

//    @Test
//    public void should_returnTwoCategories_when_getAllCategories() {
//        List<Category> categories = Arrays.asList( cat_1,cat_2 );
//
//        given( categoryRepository.findAll() ).willReturn( categories );
//
//        CategoryListDTO categoryListDTO = categoryService.getAllCategories();
//
//        then( categoryRepository ).should().findAll();
//        assertThat( categoryListDTO, is( notNullValue() ) );
//        assertThat( categoryListDTO.getCategories().size(), is(2) );
//    }

    @Test
    public void should_getOneCategory_withCorrectCategoryUrl_when_getCategory() {
        Long catId = 1L;
        given( categoryRepository.findById( anyLong() ) ).willReturn( Optional.of( cat_1 ) );

        CategoryDTO dto = categoryService.getCategory( catId );

        then( categoryRepository ).should().findById(anyLong());
        assertThat( dto, is( notNullValue() ));
        assertThat( dto.getCategoryUrl(), is( CategoryController.BASE_URL + "/1") );
    }

    @Test(expected = BadRequestException.class)
    public void should_throwBadRequestException_when_invalidID_getCategory() {
        //id does not exist
        Long catId = 9445L;
        given( categoryRepository.findById(anyLong())).willReturn( Optional.empty() );

        CategoryDTO dto = categoryService.getCategory( catId );
    }

    @Test
    public void ahould_createNewCategory_when_createNewCategoryWithValidCategoryDTO() {
        given( categoryRepository.save( any( Category.class ) )).willReturn( newCat );

        CategoryDTO dto = categoryService.createNewCategory( newCatDTO );

        then( categoryRepository ).should().save( any( Category.class ) );
        assertThat( dto, is( notNullValue()) );
        assertThat( dto.getName(), is( newCat.getName()) );
        assertThat( dto.getCategoryUrl(), is( CategoryController.BASE_URL + "/" + newCat.getId()));
    }
}