package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.UserController;
import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.PasswordRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.UserRepository;
import com.blogen.services.security.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for RESTful operations on Blogen {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private EncryptionService encryptionService;
    private UserMapper userMapper;

    @Value("${app.avatar.dir}")
    private String AVATAR_DIR;

    private static final String DEFAULT_AVATAR = "avatar0.jpg";

    @Autowired
    public UserServiceImpl( UserRepository userRepository,
                            EncryptionService encryptionService,
                            UserMapper userMapper ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @Override
    public UserListDTO getAllUsers() {
        List<UserDTO> userDTOS = userRepository.findAll()
                .stream()
                .map( user -> {
                    UserDTO dto = userMapper.userToUserDto( user );
                    dto.setUserUrl( UserService.buildUserUrl( user ) );
                    return dto;
                } ).collect( Collectors.toList());
        return new UserListDTO( userDTOS );
    }

    @Override
    public UserDTO getUser( Long id ) {
        User user = validateUserId( id );
        UserDTO userDTO = userMapper.userToUserDto( user );
        userDTO.setUserUrl( UserService.buildUserUrl( user ) );
        return userDTO;
    }


    @Transactional
    @Override
    public UserDTO updateUser( Long id,  UserDTO userDTO ) {
        User user = validateUserId( id );
        userMapper.updateUserFromDTO( userDTO, user );
        User savedUser = userRepository.save( user );
        UserDTO returnDto = userMapper.userToUserDto( savedUser );
        returnDto.setUserUrl( UserService.buildUserUrl( savedUser ) );
        return returnDto;
    }

    /**
     * save a {@link User} to the repository
     * @param user to save
     * @return the saves User entity
     */
    @Override
    public User saveUser( User user ) {
        user = checkAndEncryptPassword( user );
        return userRepository.save( user );
    }

    @Transactional
    @Override
    public void changePassword( Long id, PasswordRequestDTO dto ) {
        User user = validateUserId( id );
        user.setPassword( dto.getPassword() );
        user = checkAndEncryptPassword( user );
        User savedUser = userRepository.save( user );
        //TODO might possibly need to reset Spring's Authentication object
    }

    /**
     * gets a {@link User} by their username
     *
     * @param name - username to search for
     * @return a {@link User} having the specified username
     */
    @Override
    public Optional<User> findByUserName( String name ) {
        return Optional.ofNullable( userRepository.findByUserName( name ) );
    }

    @Override
    public Optional<User> findById( Long id ) {
        return userRepository.findById( id );
    }


    @Override
    public String buildAvatarUrl( User user ) {
        return AVATAR_DIR + "/" + user.getUserPrefs().getAvatarImage();
    }
    /**
     * encrypts the user password if it was set on the User object
     * @param user
     * @return the User object with the encryptedPassword field set
     */
    private User checkAndEncryptPassword( User user ) {
        if ( user.getPassword() != null ) {
            user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
        }
        return user;
    }

    /**
     * make sure the passed in user id exists in the repository, otherwise an exception is thrown
     * @param id
     * @return the {@link User} corresponding to the passed in ID
     * @throws BadRequestException if the user was not found in the repository
     */
    private User validateUserId( Long id ) throws BadRequestException {
        User user = userRepository.findById( id )
                .orElseThrow( () -> new BadRequestException( "user with id=" + id + " does not exist" ) );
        return user;
    }


    /**
     * build a default user preferences object with default avatar image set
     * @return
     */
    private UserPrefs buildDefaultUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAvatarImage( DEFAULT_AVATAR );
        return userPrefs;
    }



    /**
     * merge non-null fields of UserDTO into the fields of the passed in User
     * @param dto
     * @param user
     * @return
     */
//// NOTE we are now Using MapStruct NullValueCheckStrategy.ALWAYS instead of this value
//    private User mergeUserDtoToUser( UserDTO dto, User user ) {
//        if ( dto.getFirstName() != null ) user.setFirstName( dto.getFirstName() );
//        if ( dto.getLastName() != null ) user.setLastName( dto.getLastName() );
//        if ( dto.getEmail() != null ) user.setEmail( dto.getEmail() );
//        if ( dto.getPassword() != null ) user.setPassword( dto.getPassword() );
//        if ( dto.getAvatarImage() != null ) user.getUserPrefs().setAvatarImage( dto.getAvatarImage() );
//        //userName cannot be changed once a user is created;
//        return user;
//    }
}
