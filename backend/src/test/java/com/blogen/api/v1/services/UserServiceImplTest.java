package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.UserController;
import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.UserRepository;
import com.blogen.services.AvatarService;
import com.blogen.services.RoleService;
import com.blogen.services.security.PasswordEncryptionService;
import com.blogen.utils.DomainBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Unit Tests for the User REST Service
 *
 * @author Cliff
 */
public class UserServiceImplTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private AvatarService avatarService;

    @Mock
    private PasswordEncryptionService passwordEncryptionService;

    @Mock
    private Jwt jwt;

    private UserMapper userMapper = UserMapper.INSTANCE;

    User user1;
    String user1Url;
    User user2;
    String user2Url;
    User newUser;
    String newUserUrl;
    User updatedUser1;
    UserDTO newUserDTO;
    List<String> roles = Arrays.asList("USER");

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, avatarService, passwordEncryptionService, roleService, userMapper);
        user1 = DomainBuilder.buildUser(1L, "johndoe", "John", "Doe", "jdoe@gmail.com", "", "123abc");
        updatedUser1 = DomainBuilder.buildUser(1L, "johndoe", "John", "Doe", "jdoe@gmail.com", "", "123abc");
        user1Url = UserController.BASE_URL + "/1";
        user2 = DomainBuilder.buildUser(2L, "mgill", "Maggy", "McGill", "mags@hotmail.com", "", "123abc");
        user2Url = UserController.BASE_URL + "/2";
        newUserDTO = new UserDTO(3L, "new", "user", "newuser", "newby@zing.org", "password", "avatar1.jpg", roles, null);
        newUser = DomainBuilder.buildUser(3L, "newuser", "new", "user", "newby@zing.com", null, "123abc");
        newUserUrl = UserController.BASE_URL + "/3";
    }

    @Test
    public void should_returnTwoUsers_when_getAllUsers() {
        List<User> userList = Arrays.asList(user1, user2);

        given(userRepository.findAll()).willReturn(userList);

        UserListDTO userListDTO = userService.getAllUsers();

        then(userRepository).should().findAll();
        assertThat(userListDTO, is(notNullValue()));
        assertThat(userListDTO.getUsers().size(), is(2));
        assertThat(userListDTO.getUsers().get(0).getUserUrl(), is(user1Url));
    }

    @Test
    public void should_getUser1_when_getUserWithValidId() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user1));

        UserDTO userDTO = userService.getUser(1L);

        then(userRepository).should().findById(anyLong());
        assertThat(userDTO, is(notNullValue()));
        assertThat(userDTO.getUserUrl(), is(user1Url));
    }

    @Test
    public void should_throwBadRequestException_whenGetUserWithInvalidId() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.getUser(234L));
    }

//    @Test
//    public void should_createNewUser_when_createNewUser_withValidUserDTO() {
//
//        given( userRepository.save( any(User.class) )).willReturn( newUser );
//        given( encryptionService.encrypt( anyString() )).willReturn( "123abc" );
//
//        UserDTO userDTO = userService.createNewUser( newUserDTO );
//
//        then( userRepository ).should().save( any(User.class) );
//        then( encryptionService ).should().encrypt( anyString() );
//        assertThat( userDTO, is( notNullValue() ) );
//        assertThat( userDTO.getFirstName(), is( newUser.getFirstName()) );
//        assertThat( userDTO.getLastName(), is( newUser.getLastName()) );
//        assertThat( userDTO.getUserName(), is( newUser.getUserName()) );
//        assertThat( userDTO.getEmail(), is( newUser.getEmail()) );
//        assertThat( userDTO.getPassword(), is( newUser.getPassword()) );
//        assertThat( userDTO.getUserUrl(), is( newUserUrl) );
//    }

//    @Test( expected = BadRequestException.class )
//    public void should_throwBadRequestException_when_userNameAlreadyExists() {
//        given( userRepository.save( any(User.class) )).willThrow( DataIntegrityViolationException.class );
//
//        UserDTO userDTO = userService.createNewUser( newUserDTO );
//
//        then( userRepository ).should().save( any(User.class) );
//    }

    @Test
    public void should_updateFirstName_when_updateUser() {
        newUserDTO = new UserDTO();
        newUserDTO.setRoles(roles);
        newUserDTO.setFirstName("NewFirstName");
        updatedUser1.setFirstName("NewFirstName");

        given(userService.findById(1L)).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(updatedUser1);

        UserDTO savedDTO = userService.updateUser(user1, newUserDTO);

        then(userRepository).should().save(any(User.class));
        assertThat(savedDTO.getFirstName(), is(newUserDTO.getFirstName()));
        assertThat(savedDTO.getLastName(), is(user1.getLastName()));
    }
}