package com.blogen.api.v1.validators;

import com.blogen.api.v1.model.PasswordRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validate required fields when PUTing {@link com.blogen.api.v1.model.PasswordRequestDTO}
 * @author Cliff
 */
@Component
public class PasswordValidator implements Validator {


    @Override
    public boolean supports( Class<?> clazz ) {
        return PasswordRequestDTO.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        PasswordRequestDTO dto = (PasswordRequestDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "password",
                "required.password", "password is a required field but was null or empty" );


        if ( inValidLength( dto.getPassword(), 8, 255 ) )
            errors.rejectValue( "password","invalid.password","password must be >= 8 characters" );
    }

    private static boolean hasValue(String data) {
        return data != null && data.length() > 0;
    }

    private static boolean inValidLength( String data, int min, int max) {
        return data.length() < min || data.length() > max;
    }
}
