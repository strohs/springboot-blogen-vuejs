package com.blogen.api.v1.validators;

import com.blogen.api.v1.model.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate required fields when PUTing {@link UserDTO}
 * @author Cliff
 */
@Component
public class UpdateUserValidator implements Validator {


    @Override
    public boolean supports( Class<?> clazz ) {
        return UserDTO.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        UserDTO userDTO = (UserDTO) target;

        // when updating a user's details, any null or blank fields will be ignored

//        if (hasValue( userDTO.getEmail() ) && userDTO.getEmail().length() > 0 && userDTO.getEmail().length() <= 8)
//            errors.rejectValue( "email","invalid.email","email address is not valid" );

        if (hasValue( userDTO.getFirstName() ) && inValidLength( userDTO.getFirstName(), 2, 255 ) )
            errors.rejectValue( "firstName","invalid.firstName","first name must be greater than 2 characters" );

        if (hasValue( userDTO.getLastName() ) && inValidLength( userDTO.getLastName(), 2, 255 ) )
            errors.rejectValue( "lastName","invalid.lastName","last name must be greater than 2 characters" );

        // todo check if username exists, may do this in front end
        if (hasValue( userDTO.getUserName() ) && inValidLength( userDTO.getUserName(), 2, 255 ) )
            errors.rejectValue( "userName","invalid.userName","username must be greater than 2 characters" );

        if (hasValue( userDTO.getPassword() ) && inValidLength( userDTO.getPassword(), 8, 255 ) )
            errors.rejectValue( "password","invalid.password","password must be greater than 8 characters" );
    }

    private static boolean hasValue(String data) {
        return data != null && data.length() > 0;
    }

    private static boolean inValidLength( String data, int min, int max) {
        return data.length() < min || data.length() > max;
    }
}
