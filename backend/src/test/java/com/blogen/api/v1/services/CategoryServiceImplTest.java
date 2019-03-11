package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.CategoryController;
import com.blogen.api.v1.mappers.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.domain.Category;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.services.utils.PageRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    @Mock
    private CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    private Category businessCat;
    private Category healthCat;
    private Category techCat;
    private CategoryDTO businessCatDto;
    private CategoryDTO healthCatDto;
    private CategoryDTO techCatDto;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        categoryService = new CategoryServiceImpl( categoryRepository, categoryMapper, pageRequestBuilder );
        businessCat = Category.builder().id(1L).name("Business").created(LocalDateTime.now()).build();
        healthCat = Category.builder().id(2L).name("Health & Wellness").created(LocalDateTime.now()).build();
        techCat = Category.builder().id(3L).name("Technology").created(LocalDateTime.now()).build();
        businessCatDto = CategoryDTO.builder().id(1L).name("Business").build();
        healthCatDto = CategoryDTO.builder().id(2L).name("Health & Wellness").build();
        techCatDto = CategoryDTO.builder().id(3L).name("Technology").build();
    }

    @Test
    public void should_return_list_of_three_categories_when_getCategories_with_pageSize_of_3() {
        List<Category> categories = Arrays.asList(businessCat,healthCat,techCat);
        PageRequest pageRequest = PageRequest.of(0,3,Sort.Direction.DESC, "id");
        Page<Category> page = new PageImpl(categories);

        given( pageRequestBuilder.buildPageRequest(0, 3, Sort.Direction.DESC, "id")).willReturn(pageRequest);
        given( categoryMapper.categoryToCategoryDto(businessCat)).willReturn(businessCatDto);
        given( categoryMapper.categoryToCategoryDto(healthCat)).willReturn(healthCatDto);
        given( categoryMapper.categoryToCategoryDto(techCat)).willReturn(techCatDto);
        given( categoryRepository.findAllBy(pageRequest)).willReturn(page);

        CategoryListDTO categoryListDTOs = categoryService.getCategories(0,3);

        then( categoryRepository ).should().findAllBy(pageRequest);
        then( categoryMapper ).should( times(3) ).categoryToCategoryDto( any(Category.class) );
        then( pageRequestBuilder ).should().buildPageRequest(0,3, Sort.Direction.DESC, "id");
        assertThat( categoryListDTOs.getCategories(), hasSize(3) );
    }

    @Test
    public void should_return_one_category_with_CategoryUrl_when_getCategory() {

        given( categoryRepository.findById( anyLong() ) ).willReturn( Optional.of(businessCat) );
        given( categoryMapper.categoryToCategoryDto(any(Category.class))).willReturn( businessCatDto );

        CategoryDTO dto = categoryService.getCategory( 1L );

        then( categoryRepository ).should().findById(1L);
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
    public void should_createNewCategory_when_given_newCategoryDTO() {
        Category newCat = Category.builder().id(5L).name("New Category").created(LocalDateTime.now()).build();
        CategoryDTO newCatDTO = new CategoryDTO(5L,"New Category", CategoryController.BASE_URL + "/5" );

        given( categoryRepository.save( any(Category.class))).willReturn(newCat);
        given( categoryMapper.categoryDtoToCategory( any(CategoryDTO.class) )).willReturn(newCat);
        given( categoryMapper.categoryToCategoryDto( any(Category.class) )).willReturn(newCatDTO);

        // when
        CategoryDTO dto = categoryService.createNewCategory( newCatDTO );

        then( categoryRepository ).should().save( newCat );
        assertThat( dto, is( notNullValue() ));
        assertThat( dto.getId(), is(5L) );
        assertThat( dto.getName(), is("New Category") );
        assertThat( dto.getCategoryUrl(), is( CategoryController.BASE_URL + "/" + newCat.getId() ));
    }
}