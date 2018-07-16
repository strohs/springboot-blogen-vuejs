package com.blogen.repositories;

import com.blogen.domain.UserPrefs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for CRUD ops on {@link UserPrefs} data
 *
 * Author: Cliff
 */
public interface UserPrefsRepository extends JpaRepository<UserPrefs,Long> {

    UserPrefs findByUser_Id( Long userId );


}
