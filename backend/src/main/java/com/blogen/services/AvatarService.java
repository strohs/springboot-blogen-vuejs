package com.blogen.services;

import com.blogen.domain.Avatar;
import com.blogen.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Service for retrieving Avatar Image names
 *
 * @author Cliff
 */
public interface AvatarService {

    static final String DEFAULT_AVATAR = "avatar0.jpg";

    List<String> getAllAvatarImageNames();

    Optional<Avatar> getAvatarByFileName( String fileName );

    /**
     * builds a relative URL to the User's avatar image
     * @param user
     * @return a relative URL to the User's avatar
     */
    String buildAvatarUrl( User user );

}
