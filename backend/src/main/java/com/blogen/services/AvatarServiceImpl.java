package com.blogen.services;

import lombok.extern.slf4j.Slf4j;
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


    /**
     * Gets a list of all the avatar filenames in the /avatars directory.
     * Avatar filenames must start with avatar
     *
     * @return a List of filenames: e.g. avatar1.jpg,avatar2.jpg...
     */
    @Override
    public List<String> getAllAvatarImageNames() {
        Resource[] resources = new Resource[ 0 ];
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            resources = resolver.getResources( AVATAR_IMG_DIR );
        } catch ( IOException e ) {
            log.error("error getting resources from WebApplicationContext: " + e.getMessage() );
        }
        List<String> imageNames =
                Arrays.stream( resources )
                        .filter( this::isFile )
                        .map( Resource::getFilename )
                        .collect( Collectors.toList());
        return imageNames;
    }

    private boolean isFile( Resource resource ) {
        boolean isFile = false;
        try {
            isFile = resource.getFile().isFile();
        } catch ( IOException e ) {
            log.error( "error checking if file in /avatars directory isFile: " + e.getMessage() );
        }
        return isFile;
    }
}
