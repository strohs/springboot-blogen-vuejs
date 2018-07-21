package com.blogen.api.v1.validators;

import com.blogen.api.v1.model.PostRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validate required fields of a {@link com.blogen.api.v1.model.PostDTO}
 * @author Cliff
 */
@Component
public class PostRequestDtoValidator implements Validator {


    @Override
    public boolean supports( Class<?> clazz ) {
        return PostRequestDTO.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        PostRequestDTO postDTO = (PostRequestDTO) target;

        //these are PostRequestDTO fields which are required
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "title","required.title","title is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "text","required.text","text is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "categoryId","required.categoryName","categoryId is a required field but was null or empty" );

    }
}
