package com.blogen.services;

import com.blogen.domain.User;

import java.util.Optional;

/**
 *
 * @author Cliff
 */
public interface PrincipalService {

    Optional<String> getPrincipalUserName();

    Optional<Long> getPrincipalUserId();

}
