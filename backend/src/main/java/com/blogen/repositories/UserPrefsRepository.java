package com.blogen.repositories;

import com.blogen.domain.UserPrefs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for CRUD ops on {@link UserPrefs} data
 *
 * Author: Cliff
 */
public interface UserPrefsRepository extends JpaRepository<UserPrefs,Long> {

    UserPrefs findByUser_Id( Long userId );

    @Query("SELECT DISTINCT t.avatarImage FROM UserPrefs t")
    List<String> findDistinctAvatarImages();

}
