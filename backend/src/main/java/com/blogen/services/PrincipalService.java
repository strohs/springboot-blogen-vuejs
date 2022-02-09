package com.blogen.services;

import java.util.Optional;

/**
 *
 * @author Cliff
 */
public interface PrincipalService {

    Optional<String> getPrincipalUserName();

    Optional<Long> getPrincipalUserId();

}
