package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.UserController;
import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.Role;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.services.RoleService;
import com.blogen.services.security.EncryptionService;
import com.blogen.services.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Author: Cliff
 */
public class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private UserMapper userMapper = UserMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        authorizationService = new AuthorizationServiceImpl( userService, roleService, userMapper,
                encryptionService, authenticationManager, jwtTokenProvider);
    }

    @Test
    public void should_returnNewUserDTO_when_validUserDTO() {
        long newId = 99L;
        UserDTO validDTO = buildUserDTO();
        User savedUser = buildUser( newId );
        Role userRole = Builder.buildRole( 1L,"USER" );
        UserDTO savedDTO = buildUserDTO();
        savedUser.addRole( userRole );
        savedDTO.setUserUrl( UserController.BASE_URL + "/" + newId );

        given( userService.saveUser( any( User.class ) )).willReturn( savedUser );
        given( roleService.getByName( anyString() )).willReturn( userRole );
        given( encryptionService.encrypt( anyString() ) ).willReturn( "{bcrypt}dfdf34343" );
        given( userService.buildUserUrl( any(User.class) )).willReturn( UserController.BASE_URL + "/" + newId );

        UserDTO newUser = authorizationService.signUpUser( validDTO );

        then( userService ).should().saveUser( any( User.class) );
        then( roleService ).should().getByName( anyString() );
        then( encryptionService ).should().encrypt( anyString() );
        then( userService ).should().buildUserUrl( any(User.class) );
        assertThat( newUser, is( notNullValue()) );
        assertThat( newUser.getUserUrl(), is( savedDTO.getUserUrl()) );
    }

    @Test( expected = BadRequestException.class )
    public void should_throwException_when_signupUser_with_usernameThatExists() {
        String userName = "userexists";
        UserDTO userDTO = buildUserDTO();
        userDTO.setUserName( userName );
        Role userRole = Builder.buildRole( 1L,"USER" );

        given( roleService.getByName( anyString() )).willReturn( userRole );
        given( userService.saveUser( any( User.class ))).willThrow( DataIntegrityViolationException.class );
        given( encryptionService.encrypt( anyString() ) ).willReturn( "{bcrypt}dfdf34343" );

        UserDTO newUser = authorizationService.signUpUser( userDTO );
        then( userService ).should().saveUser( any( User.class) );
        then( encryptionService ).should().encrypt( anyString() );
    }

    @Test
    public void loginUser() {
    }




    private UserDTO buildUserDTO() {
        return UserDTO.builder()
                .firstName( "John" )
                .lastName( "Smith" )
                .email( "jsmith@gmail.com" )
                .userName( "johnsmith" )
                .roles( Arrays.asList("USER") )
                .password( "secret" )
                .build();
    }

    private User buildUser( Long id ) {
        return Builder.buildUser( id, "johnsmith","John","Smith","jsmith@gmail.com",
                "password","{bcrypt}34sf23");
    }

    private LoginRequestDTO buildLoginDTO() {
        return LoginRequestDTO.builder().username( "johnsmith" ).password( "password" ).build();
    }
}