package com.blogen.repositories;

import com.blogen.domain.Avatar;

import java.util.List;
import java.util.Optional;

/**
 * Author: Cliff
 */
public interface AvatarRepository {

    Avatar save(Avatar avatar);

    Optional<Avatar> findById(Long id);

    Optional<Avatar> findByFileName( String fileName );

    List<String> findAllAvatarFileNames();
}
