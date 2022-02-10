package com.blogen.api.v1.services;

import com.blogen.api.v1.model.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CategoryServiceITest {


    @Autowired
    CategoryService categoryService;

    @Test
    @WithMockUser(username = "3", authorities = {"SCOPE_ROLE_API", "SCOPE_ROLE_ADMIN"})
    public void should_allow_admins_to_create_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();
        final CategoryDTO newCategory = categoryService.createNewCategory(newDto);
        assertThat(newCategory, is(notNullValue()));
    }

    @Test
    @WithMockUser(username = "3", authorities = {"SCOPE_ROLE_API", "SCOPE_ROLE_USER"})
    public void should_throw_accessDenied_when_user_tries_to_create_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();

        assertThrows(AccessDeniedException.class, () -> categoryService.createNewCategory(newDto));
    }

    @Test
    @WithMockUser(username = "3", authorities = {"SCOPE_ROLE_API", "SCOPE_ROLE_ADMIN"})
    public void should_allow_admins_to_update_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Tech Category").build();
        final CategoryDTO newCategory = categoryService.updateCategory(1L, newDto);
        assertThat(newCategory, is(notNullValue()));
    }

    @Test
    @WithMockUser(username = "3", authorities = {"SCOPE_ROLE_API", "SCOPE_ROLE_USER"})
    public void should_throw_accessDenied_when_user_tries_to_update_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();

        assertThrows(AccessDeniedException.class, () -> categoryService.updateCategory(1L, newDto));
    }


}
