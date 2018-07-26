package com.blogen.services;

import com.blogen.repositories.UserPrefsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Services for working with Avatar image files
 *
 * @author Cliff
 */
@Slf4j
@Service
public class AvatarServiceImpl implements AvatarService {

    //TODO move this to properties file
    //image directory containing Avatar Images
    private static final String AVATAR_IMG_DIR = "classpath:static/avatars*";

    private UserPrefsRepository userPrefsRepository;

    @Autowired
    public AvatarServiceImpl( UserPrefsRepository userPrefsRepository ) {
        this.userPrefsRepository = userPrefsRepository;
    }

    /**
     * Gets a list of all the distince avatar filenames
     *
     * @return a List of filenames: e.g. avatar1.jpg,avatar2.jpg...
     */
    @Override
    public List<String> getAllAvatarImageNames() {
        return userPrefsRepository.findDistinctAvatarImages();
    }

}
