package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.CategoryController;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.domain.Category;

/**
 * CategoryService for REST API
 *
 * @author Cliff
 */
public interface CategoryService {

    /**
     *
     * @return a List of all Blogen categories, in a {@link CategoryDTO}
     */
    CategoryListDTO getAllCategories();

    /**
     * Get a specific Category
     *
     * @param id the Category id to search for
     * @return a CategoryDTO representing the Blogen {@link com.blogen.domain.Category}
     */
    CategoryDTO getCategory( Long id );

    /**
     * Create a new Blogen Category
     * @param categoryDTO containes the details of the Category to create
     * @return a CategoryDTO representing the newly created Category
     */
    CategoryDTO createNewCategory( CategoryDTO categoryDTO );


    static String buildCategoryUrl( Category category ) {
        return CategoryController.BASE_URL + "/" + category.getId();
    }
}
