package com.blogen.api.v1.validators;

import com.blogen.api.v1.model.CategoryDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for CategoryDTOs
 *
 * @author Cliff
 */
@Component
public class CategoryDtoValidator implements Validator {

    @Override
    public boolean supports( Class<?> clazz ) {
        return CategoryDTO.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {

        CategoryDTO categoryDTO = (CategoryDTO) target;

        //these are dto fields which are required
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name","required.name","category name is a required field but was null or empty" );

        //category id will be ignored...for now
        //if ( categoryDTO.getId() != null )
        //    errors.rejectValue( "id","invalid.id","category id should not be sent as part of the request body" );

        //categoryUrl should not be sent in a request
        if ( categoryDTO.getCategoryUrl() != null )
            errors.rejectValue( "categoryUrl","invalid.categoryUrl","categoryUrl should not be sent as part of the request body" );
    }
}
