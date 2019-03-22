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
import com.blogen.services.security.BlogenJwtService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

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
    private OAuth2MappingService oAuth2MappingService;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private BlogenJwtService jwtTokenProvider;

    private UserMapper userMapper = UserMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        authorizationService = new AuthorizationServiceImpl( userService, userMapper,
                encryptionService, oAuth2MappingService, jwtTokenProvider);
    }

    @Test
    public void should_create_new_user_when_signUpUser_with_validUserDTO() {
        long newId = 99L;
        UserDTO validDTO = buildUserDTO();
        User savedUser = buildUser( newId );
        Role userRole = Builder.buildRole( 1L,"USER" );
        UserDTO savedDTO = buildUserDTO();
        savedUser.addRole( userRole );
        savedDTO.setUserUrl( UserController.BASE_URL + "/" + newId );

        given( userService.createNewUser( validDTO )).willReturn( savedUser );

        UserDTO newUser = authorizationService.signUpUser( validDTO );

        then( userService ).should().createNewUser( validDTO );
        assertThat( newUser, is( notNullValue()) );
        assertThat( newUser.getUserUrl(), is( savedDTO.getUserUrl()) );
    }

    @Test( expected = BadRequestException.class )
    public void should_throwException_when_signupUser_with_usernameThatExists() {
        String userName = "userexists";
        UserDTO userDTO = buildUserDTO();
        userDTO.setUserName( userName );
        Role userRole = Builder.buildRole( 1L,"USER" );

        given( userService.createNewUser( userDTO )).willThrow( IllegalArgumentException.class );
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