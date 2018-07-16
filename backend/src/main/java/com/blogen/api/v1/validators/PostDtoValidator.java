package com.blogen.api.v1.validators;

import com.blogen.api.v1.model.PostDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validate required fields of a {@link com.blogen.api.v1.model.PostDTO}
 * @author Cliff
 */
@Component
public class PostDtoValidator implements Validator {


    @Override
    public boolean supports( Class<?> clazz ) {
        return PostDTO.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        PostDTO postDTO = (PostDTO) target;

        //these are PostDTO fields which are required
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "title","required.title","title is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "text","required.text","text is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "userName","required.userName","username is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "categoryName","required.categoryName","categoryName is a required field but was null or empty" );

        //these are fields which should not be sent as part of the request
        if ( postDTO.getChildren() != null && postDTO.getChildren().size() > 0 )
            errors.rejectValue( "children", "invalid.children","children array should not be sent as part of the request" );

        if ( postDTO.getPostUrl() != null )
            errors.rejectValue( "postUrl","invalid.postUrl","postUrl should not be sent as part of the request" );

        if ( postDTO.getParentPostUrl() != null )
            errors.rejectValue( "parentPostUrl","invalid.parentPostUrl","parentPostUrl should not be sent as part of the request" );
    }
}
