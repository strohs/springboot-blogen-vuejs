package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.api.v1.services.CategoryService;
import com.blogen.api.v1.validators.CategoryDtoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for REST operations on {@link com.blogen.domain.Category}
 *
 * @author Cliff
 */
//TODO try to match security scheme to a SecurityRequirement for each method of this controller
//@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "Categories", description = "operations on categories")
@Slf4j
@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";

    private CategoryService categoryService;
    private CategoryDtoValidator categoryDtoValidator;

    @Autowired
    public CategoryController(CategoryService categoryService,
                              CategoryDtoValidator categoryDtoValidator) {
        this.categoryService = categoryService;
        this.categoryDtoValidator = categoryDtoValidator;
    }

    @InitBinder("categoryDTO")
    public void setUpBinder(WebDataBinder binder) {
        binder.addValidators(categoryDtoValidator);
    }

    @Operation(summary = "get a page of categories")
    @GetMapping(produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public CategoryListDTO getCategories(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                         @RequestParam(value = "limit", defaultValue = "3") int pageLimit) {
        log.debug("getCategoryPage pageNum={} limit={}", pageNum, pageLimit);
        return categoryService.getCategories(pageNum, pageLimit);
    }

    @Operation(summary = "get a specific category by id")
    @GetMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getCategory(@PathVariable("id") Long id) {
        log.debug("getCategory id=" + id);
        return categoryService.getCategory(id);
    }

    @Operation(summary = "create a new category")
    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createNewCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.debug("createNewCategory categoryDTO=" + categoryDTO);
        return categoryService.createNewCategory(categoryDTO);
    }

    @Operation(summary = "replace an existing category with new category data")
    @PutMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        log.debug("updateCategory id=" + id + " categoryDTO:\n" + categoryDTO);
        return categoryService.updateCategory(id, categoryDTO);
    }
}
