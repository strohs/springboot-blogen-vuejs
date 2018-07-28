package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.api.v1.services.CategoryService;
import com.blogen.api.v1.validators.CategoryDtoValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for REST operations on {@link com.blogen.domain.Category}
 *
 * @author Cliff
 */
@Api
@Slf4j
@RestController
@RequestMapping( CategoryController.BASE_URL )
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";

    private CategoryService categoryService;
    private CategoryDtoValidator categoryDtoValidator;

    @Autowired
    public CategoryController( CategoryService categoryService,
                               CategoryDtoValidator categoryDtoValidator ) {
        this.categoryService = categoryService;
        this.categoryDtoValidator = categoryDtoValidator;
    }

    @InitBinder("categoryDTO")
    public void setUpBinder( WebDataBinder binder ) {
        binder.addValidators( categoryDtoValidator );
    }

//    @ApiOperation( value = "get a list of all categories", produces = "application/json")
//    @GetMapping
//    @ResponseStatus( HttpStatus.OK )
//    public CategoryListDTO getAllCategories() {
//        log.debug( "getAllCategories" );
//        return categoryService.getAllCategories();
//    }

    @ApiOperation( value = "get a page of categories", produces = "application/json")
    @GetMapping
    @ResponseStatus( HttpStatus.OK )
    public CategoryListDTO getCategories( @RequestParam(value = "page", defaultValue = "0") int pageNum,
                                            @RequestParam(value = "limit", defaultValue = "3") int pageLimit) {
        log.debug( "getCategoryPage pageNum={} limit={}",pageNum, pageLimit );
        return categoryService.getCategories( pageNum, pageLimit );
    }

    @ApiOperation( value = "get a specific category by id", produces = "application/json")
    @GetMapping( "/{id}")
    @ResponseStatus( HttpStatus.OK )
    public CategoryDTO getCategory( @PathVariable("id") Long id ) {
        log.debug( "getCategory id=" + id );
        return categoryService.getCategory( id );
    }

    @ApiOperation( value = "create a new category", produces = "application/json", consumes = "application/json")
    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public CategoryDTO createNewCategory( @Valid @RequestBody CategoryDTO categoryDTO ) {
        log.debug( "createNewCategory categoryDTO=" + categoryDTO );
        return categoryService.createNewCategory( categoryDTO );
    }

    @ApiOperation( value = "replace an existing category with new category data", consumes = "application/json", produces = "application/json")
    @PutMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public CategoryDTO updateCategory( @PathVariable("id") Long id, @Valid @RequestBody CategoryDTO categoryDTO ) {
        log.debug( "updateCategory id=" + id + " categoryDTO:\n" + categoryDTO );
        return categoryService.updateCategory( id, categoryDTO );
    }
}
