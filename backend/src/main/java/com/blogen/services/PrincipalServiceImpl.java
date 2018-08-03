package com.blogen.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Operations for working with Security Principals
 *
 * @author Cliff
 */
@Service
public class PrincipalServiceImpl implements PrincipalService {

    @Override
    public String getPrincipalUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
