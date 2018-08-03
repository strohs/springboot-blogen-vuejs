package com.blogen.services;

import com.blogen.domain.Avatar;
import com.blogen.domain.User;
import com.blogen.exceptions.NotFoundException;
import com.blogen.repositories.AvatarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Services for working with Avatar image files
 *
 * @author Cliff
 */
@Slf4j
@Service
public class AvatarServiceImpl implements AvatarService {

    @Value("${app.avatar.dir}")
    private String AVATAR_DIR;

    private AvatarRepository avatarRepository;

    @Autowired
    public AvatarServiceImpl( AvatarRepository avatarRepository ) {
        this.avatarRepository = avatarRepository;
    }

    /**
     * Gets a list of all the distince avatar filenames
     *
     * @return a List of filenames: e.g. avatar1.jpg,avatar2.jpg...
     */
    @Override
    public List<String> getAllAvatarImageNames() {
        return avatarRepository.findAllAvatarFileNames();
    }

    @Override
    public Optional<Avatar> getAvatarByFileName( String fileName ) {
        Optional<Avatar> opa = Optional.empty();
        if ( fileName != null ) {
            opa = avatarRepository.findByFileName( fileName );
        }
        return opa;
    }


    @Override
    public String buildAvatarUrl( User user ) {
        return AVATAR_DIR + "/" + user.getUserPrefs().getAvatar().getFileName();
    }

}
