package com.blogen.api.v1.services;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.services.security.BlogenAuthority;
import com.blogen.services.security.WithMockJwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CategoryServiceITest {


    @Autowired
    CategoryService categoryService;

    @Test
    @WithMockJwt(subject = "3", scopes = {BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_ADMIN})
    public void should_allow_admins_to_create_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();
        final CategoryDTO newCategory = categoryService.createNewCategory(newDto);
        assertThat(newCategory, is(notNullValue()));
    }

    @Test
    @WithMockJwt(subject = "3", scopes = {BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_USER})
    public void should_throw_accessDenied_when_user_tries_to_create_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();

        assertThrows(AccessDeniedException.class, () -> categoryService.createNewCategory(newDto));
    }

    @Test
    @WithMockJwt(subject = "3", scopes = {BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_ADMIN})
    public void should_allow_admins_to_update_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Tech Category").build();
        final CategoryDTO newCategory = categoryService.updateCategory(1L, newDto);
        assertThat(newCategory, is(notNullValue()));
    }

    @Test
    @WithMockJwt(subject = "3", scopes = {BlogenAuthority.ROLE_API, BlogenAuthority.ROLE_USER})
    public void should_throw_accessDenied_when_user_tries_to_update_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();

        assertThrows(AccessDeniedException.class, () -> categoryService.updateCategory(1L, newDto));
    }


}
