package com.blogen.services;

import com.blogen.api.v1.services.UserService;
import com.blogen.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

/**
 * Operations for getting user names from Spring Security Principals
 * Blogen mainly uses a JWT to store the user's ID in the "subject" claim. This class retrieves the id from the
 * subject and uses it to retrieve the UserName from the User table
 *
 * Blogen use to use a Spring UserDetails as the principal. It may use it again, so we are keeping that logic for
 * potential future use.
 *
 * @author Cliff
 */
@Service
public class PrincipalServiceImpl implements PrincipalService {

    @Autowired
    private UserService userService;

    @Override
    public Optional<String> getPrincipalUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ) {
            return Optional.of( ((UserDetails)principal).getUsername() );
        } else if (principal instanceof Jwt){
            Jwt jwt = (Jwt) principal;
            Long id = Long.parseLong( (String)jwt.getClaims().get("subject") );
            return userService.findById(id).map( user -> user.getUserName() );
        } else {
            return Optional.ofNullable(principal.toString());
        }
    }

    @Override
    public Optional<Long> getPrincipalUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return Optional.of( Long.parseLong( (String)jwt.getClaims().get("sub") ) );
        } else {
            return Optional.empty();
        }
    }

}
