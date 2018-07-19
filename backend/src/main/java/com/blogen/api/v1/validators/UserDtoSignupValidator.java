package com.blogen.api.v1.validators;

import com.blogen.api.v1.model.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validate required fields of a {@link UserDTO}
 * @author Cliff
 */
@Component
public class UserDtoSignupValidator implements Validator {


    @Override
    public boolean supports( Class<?> clazz ) {
        return UserDTO.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        UserDTO userDTO = (UserDTO) target;

        //these are UserDTO fields which are required for sign-up
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "firstName","required.firstName","firstName is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "lastName","required.lastName","lastName is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "userName","required.userName","username is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "email","required.email","email is a required field but was null or empty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "password","required.password","password is a required field but was null or empty" );

        //TODO Validate Avatar Image names

    }
}
