package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.domain.Category;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.services.utils.PageRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Category Service for REST API
 * For Categories:
 *   we can get a list of all categories
 *   get a specific category
 *   create a new category
 *
 * NOTE: Deleting or Updating a category is not supported in this API
 * @author Cliff
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private PageRequestBuilder pageRequestBuilder;

    @Autowired
    public CategoryServiceImpl( CategoryRepository categoryRepository, CategoryMapper categoryMapper,
                                PageRequestBuilder pageRequestBuilder ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.pageRequestBuilder = pageRequestBuilder;
    }

//    @Override
//    public CategoryListDTO getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        List<CategoryDTO> dtos = categories
//                .stream()
//                .map( category -> {
//                    CategoryDTO dto = categoryMapper.categoryToCategoryDto( category );
//                    dto.setCategoryUrl( CategoryService.buildCategoryUrl( category ) );
//                    return dto;
//                } ).collect( Collectors.toList());
//        return new CategoryListDTO( dtos, null );
//    }

    @Override
    public CategoryListDTO getCategories( int pageNum, int pageSize ) {
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( pageNum, pageSize, Sort.Direction.DESC,"id" );
        //retrieve the posts
        Page<Category> page = categoryRepository.findAllBy( pageRequest );

        List<CategoryDTO> catDTOS = new ArrayList<>();
        page.forEach( cat -> {
            CategoryDTO dto = categoryMapper.categoryToCategoryDto( cat );
            catDTOS.add( dto );
        } );
        return new CategoryListDTO( catDTOS, PageRequestBuilder.buildPageInfoResponse( page ) );
    }

    @Override
    public CategoryDTO getCategory( Long id ) {
        Category category = categoryRepository.findById( id ).orElseThrow( () ->
                new BadRequestException( "category with id: " + id + " does not exist" ) );
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDto( category );
        categoryDTO.setCategoryUrl( CategoryService.buildCategoryUrl( category ) );
        return categoryDTO;
    }

    //TODO only administrators can create categories
    @Override
    public CategoryDTO createNewCategory( CategoryDTO categoryDTO ) {
        CategoryDTO savedDTO = null;
        try {
            Category categoryToSave = categoryMapper.categoryDtoToCategory( categoryDTO );
            Category savedCategory = categoryRepository.save( categoryToSave );
            savedDTO = categoryMapper.categoryToCategoryDto( savedCategory );
            savedDTO.setCategoryUrl( CategoryService.buildCategoryUrl( savedCategory ) );
        } catch ( DataIntegrityViolationException e ) {
            String message = String.format( "Category already exists with name %s", categoryDTO.getName() );
            throw new DataIntegrityViolationException( message );
        }
        return savedDTO;
    }

    //TODO only admins can update categories
    @Override
    public CategoryDTO updateCategory( Long id, CategoryDTO categoryDTO ) {
        Category category = categoryRepository.findById( id )
                .orElseThrow( () -> new BadRequestException( "category does not exist with id:" + id ) );
        // update 'category' with values from any non-null fields in the DTO
        categoryMapper.updateCategoryFromCategoryDTO( categoryDTO, category );
        Category savedCategory = categoryRepository.save( category );
        return categoryMapper.categoryToCategoryDto( savedCategory );
    }
    

}
