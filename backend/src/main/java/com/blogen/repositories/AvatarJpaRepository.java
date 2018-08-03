package com.blogen.repositories;

import com.blogen.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA implementation of the Avatar repository
 *
 * Author: Cliff
 */
public interface AvatarJpaRepository extends JpaRepository<Avatar,Long>, AvatarRepository {

    //Optional<Avatar> findById( Long id );

    Optional<Avatar> findByFileName( String fileName );

    @Query("SELECT DISTINCT t.fileName FROM Avatar t ORDER BY t.fileName")
    List<String> findAllAvatarFileNames();
}