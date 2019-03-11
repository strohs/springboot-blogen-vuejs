package com.blogen.api.v1.services;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.security.WithMockJwt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceIT {


    @Autowired
    CategoryService categoryService;

    @Test
    @WithMockJwt(subject = "3",scopes = {"API","ADMIN"})
    public void should_allow_admins_to_create_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();
        final CategoryDTO newCategory = categoryService.createNewCategory(newDto);
        assertThat( newCategory, is( notNullValue()));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockJwt(subject = "3",scopes = {"API","USER"})
    public void should_throw_accessDenied_when_user_tries_to_create_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();
        final CategoryDTO newCategory = categoryService.createNewCategory(newDto);
    }

    @Test
    @WithMockJwt(subject = "3",scopes = {"API","ADMIN"})
    public void should_allow_admins_to_update_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Tech Category").build();
        final CategoryDTO newCategory = categoryService.updateCategory(1L, newDto);
        assertThat( newCategory, is( notNullValue()));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockJwt(subject = "3",scopes = {"API","USER"})
    public void should_throw_accessDenied_when_user_tries_to_update_new_category() {
        CategoryDTO newDto = CategoryDTO.builder().name("New Category").build();
        final CategoryDTO newCategory = categoryService.updateCategory(1L,newDto);
    }



}
