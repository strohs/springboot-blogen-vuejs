package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.PasswordRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.domain.Avatar;
import com.blogen.domain.Role;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.UserRepository;
import com.blogen.services.AvatarService;
import com.blogen.services.RoleService;
import com.blogen.services.security.PasswordEncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private AvatarService avatarService;
    private PasswordEncryptionService encryptionService;
    private RoleService roleService;
    private UserMapper userMapper;



    @Autowired
    public UserServiceImpl( UserRepository userRepository,
                            AvatarService avatarService,
                            PasswordEncryptionService encryptionService,
                            RoleService roleService,
                            UserMapper userMapper ) {
        this.userRepository = userRepository;
        this.avatarService = avatarService;
        this.userMapper = userMapper;
        this.roleService = roleService;
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

    @Override
    public User createNewUser(UserDTO userDTO) throws IllegalArgumentException {
        if (findByUserName( userDTO.getUserName() ).isPresent() ) {
            throw new IllegalArgumentException("user with userName=" + userDTO.getUserName() + " already exists");
        } else {
            // create a new user
            Role userRole = roleService.getByName( "ROLE_USER" );
            Role apiRole = roleService.getByName("ROLE_API");
            User user = userMapper.userDtoToUser( userDTO );
            user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
            user.addRole( userRole );
            user.addRole( apiRole );
            UserPrefs prefs = buildDefaultUserPrefs();
            user.setUserPrefs( prefs );
            return saveUser( user );
        }
    }

    // only the authenticated user can update their own user information, OR admins can always change user info
    @Override
    @PreAuthorize( "hasAuthority('SCOPE_ROLE_ADMIN') || #user.getId().toString() == authentication.name" )
    public UserDTO updateUser(User user, UserDTO userDTO) {
        userMapper.updateUserFromDTO( userDTO, user );
        if ( userDTO.getAvatarImage() != null ) {
            Avatar avatar = avatarService.getAvatarByFileName( userDTO.getAvatarImage() )
                    .orElseThrow( () -> new BadRequestException( "avatar image does not exits:" + userDTO.getAvatarImage() ) );
            user.getUserPrefs().setAvatar( avatar );
        }
        User savedUser = userRepository.save( user );
        UserDTO returnDto = userMapper.userToUserDto( savedUser );
        returnDto.setUserUrl( UserService.buildUserUrl( savedUser ) );
        return returnDto;
    }


    @Override
    public User saveUser( User user ) {
        checkAndEncryptPassword( user );
        return userRepository.save( user );
    }

    // only the authenticated user can change their password, OR admins can change all passwords
    @PreAuthorize( "hasAuthority('SCOPE_ROLE_ADMIN') || #user.getId().toString() == authentication.name" )
    @Transactional
    @Override
    public void changePassword( User user, PasswordRequestDTO dto) {
        user.setPassword( dto.getPassword() );
        checkAndEncryptPassword( user );
        userRepository.save( user );
    }

    @Override
    public Optional<User> findByUserName( String name ) {
        return Optional.ofNullable( userRepository.findByUserName( name ) );
    }

    @Override
    public Optional<User> findById( Long id ) {
        return userRepository.findById( id );
    }



    /**
     * encrypts the user password if it was set on the User object
     * @param user
     * @return the User object with the encryptedPassword field set
     */
    private void checkAndEncryptPassword( User user ) {
        if ( user.getPassword() != null ) {
            user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
        }
    }

    /**
     * make sure the passed in user id exists in the repository, otherwise an exception is thrown
     * @param id
     * @return the {@link User} corresponding to the passed in ID
     * @throws BadRequestException if the user was not found in the repository
     */
    public User validateUserId( Long id ) throws BadRequestException {
        User user = userRepository.findById( id )
                .orElseThrow( () -> new BadRequestException( "user does not exist with id:" + id ) );
        return user;
    }


    /**
     * builds a default user preferences object with default avatar image
     * @return a UserPrefs object
     */
    public UserPrefs buildDefaultUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        Avatar defaultAvatar = avatarService.getAvatarByFileName( AvatarService.DEFAULT_AVATAR )
                .orElseThrow( () -> new BadRequestException( "default avatar image not found" ) );
        userPrefs.setAvatar( defaultAvatar );
        return userPrefs;
    }


}
