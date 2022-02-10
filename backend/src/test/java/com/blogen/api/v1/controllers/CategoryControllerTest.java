package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.services.CategoryService;
import com.blogen.api.v1.validators.CategoryDtoValidator;
import com.blogen.exceptions.BadRequestException;
import com.blogen.services.security.BlogenAuthority;
import com.blogen.services.security.WithMockJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.blogen.api.v1.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Tests for CategoryRestController
 *
 * @author Cliff
 */
@WebMvcTest(controllers = {CategoryController.class})
@Import(CategoryDtoValidator.class)
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    private CategoryDTO catDto_1;
    private CategoryDTO catDto_2;
    private CategoryDTO newCatDto;

    @BeforeEach
    public void setUp() throws Exception {
        catDto_1 = new CategoryDTO(1L, "Category1", CategoryController.BASE_URL + "/1");
        catDto_2 = new CategoryDTO(2L, "Category2", null);
        newCatDto = new CategoryDTO(2L, "Category2", CategoryController.BASE_URL + "/2");
    }

//    @Test
//    public void should_getAllCategoriesAndReturnOK_when_getAllCategories() throws Exception {
//        CategoryListDTO categoryListDTO = new CategoryListDTO( Arrays.asList( catDto_1, catDto_2), null );
//
//        given( categoryService.getAllCategories() ).willReturn( categoryListDTO );
//
//        mockMvc.perform( get( CategoryController.BASE_URL ) )
//                .andExpect( status().isOk() )
//                .andExpect( jsonPath( "$.categories", hasSize(2) ) );
//    }

    @Test
    @WithMockUser(username = "1", authorities={"SCOPE_ROLE_API", "SCOPE_ROLE_USER"})
    public void should_getOneCategoryWithCategoryUrl_when_getCategory() throws Exception {

        given(categoryService.getCategory(anyLong())).willReturn(catDto_1);

        mockMvc.perform(get(CategoryController.BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryUrl", is(catDto_1.getCategoryUrl())));
    }

    @Test
    @WithMockUser(username = "1", authorities={"SCOPE_ROLE_API", "SCOPE_ROLE_USER"})
    public void should_returnHttpBadRequest_when_getCategoryWithInvalidId() throws Exception {

        given(categoryService.getCategory(anyLong())).willThrow(new BadRequestException("invalid category id"));

        mockMvc.perform(get(CategoryController.BASE_URL + "/56234"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.globalError[0].message", is(notNullValue())));
    }

    @Test
    @WithMockUser(username = "1", authorities={"SCOPE_ROLE_API", "SCOPE_ROLE_ADMIN"})
    public void should_returnCreatedAndSetCategoryURL_when_createNewCategory() throws Exception {
        given(categoryService.createNewCategory(any(CategoryDTO.class))).willReturn(newCatDto);

        mockMvc.perform(post(CategoryController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(catDto_2))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Category2")))
                .andExpect(jsonPath("$.categoryUrl", is(newCatDto.getCategoryUrl())));
    }

    @Test
    @WithMockUser(username = "1", authorities={"SCOPE_ROLE_API", "SCOPE_ROLE_ADMIN"})
    public void should_returnUnprocessableEntity_when_createNewCategory_withInvalidRequestParam() throws Exception {
        catDto_1.setName(null);

        given(categoryService.createNewCategory(any(CategoryDTO.class))).willReturn(newCatDto);

        mockMvc.perform(post(CategoryController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(catDto_1))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.fieldError[0].message", not(is(emptyOrNullString()))));
    }
}