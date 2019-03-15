package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.LoginResponse;
import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.domain.Role;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.exceptions.BadRequestException;
import com.blogen.services.RoleService;
import com.blogen.services.security.BlogenAuthority;
import com.blogen.services.security.EncryptionService;
import com.blogen.services.security.BlogenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for signing-up new users and for logging in users
 *
 * Author: Cliff
 */
@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    private UserService userService;
    private RoleService roleService;
    private UserMapper userMapper;
    private EncryptionService encryptionService;
    private BlogenJwtService tokenService;

    private static final String DEFAULT_AVATAR_IMAGE = "avatar0.jpg";

    @Autowired
    public AuthorizationServiceImpl(UserService userService, RoleService roleService,
                                    UserMapper userMapper, EncryptionService encryptionService,
                                    BlogenJwtService tokenService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
        this.tokenService = tokenService;
    }
    
    @Override
    public UserDTO signUpUser( UserDTO userDTO ) {
        //TODO check for existing username
        //required fields validated in controller
        Role userRole = roleService.getByName( "USER" );
        User user = userMapper.userDtoToUser( userDTO );
        user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
        user.addRole( userRole );
        UserPrefs prefs = userService.buildDefaultUserPrefs();
        user.setUserPrefs( prefs );
        User savedUser;
        try {
            savedUser = userService.saveUser( user );
        } catch ( DataIntegrityViolationException ex ) {
            throw new BadRequestException( "user with userName=" + userDTO.getUserName() + " already exists" );
        }
        UserDTO returnDto = userMapper.userToUserDto( savedUser );
        //returnDto.setPassword( "" );
        returnDto.setUserUrl( UserService.buildUserUrl( savedUser ) );
        return returnDto;
    }

    @Override
    public LoginResponse authenticateAndLoginUser(LoginRequestDTO loginDTO ) {
        // check if username exists
        final User user = userService.findByUserName(loginDTO.getUsername())
                .orElseThrow(() -> new BadCredentialsException("bad username or password"));
        // check if password matches
        if (!encryptionService.checkPassword( loginDTO.getPassword(), user.getEncryptedPassword() )) {
            throw new BadCredentialsException("bad username or password");
        }
        // create a JWT from User data and grant them access to the API
        String token = buildJwt(user);
        return new LoginResponse( token, userMapper.userToUserDto( user ));
    }

    @Override
    public String authenticateAndLoginUser(String username, String password) {
        // check if username exists
        final User user = userService.findByUserName(username)
                .orElseThrow(() -> new BadCredentialsException("bad username or password"));
        // check if password matches
        if (!encryptionService.checkPassword( password, user.getEncryptedPassword() )) {
            throw new BadCredentialsException("bad username or password");
        }
        // user is valid, generate a token from User data and grant them access to the API
        return buildJwt(user);
    }

    @Override
    public String loginGithubUser(OAuth2User oAuth2User) {
        // check if this OAuth user already has an account with Blogen
        String username = AuthorizationService.GITHUB_USER_PREFIX + oAuth2User.getAttributes().get("login");
        if ( userNameExists(username) ) {
            log.debug("github oauth2 user exists: {}, logging them in and generating token", username);
            User user = userService.findByUserName(username).get();
            return buildJwt( user );
        } else {
            // create a new user
            UserDTO newUserDto = userMapper.githubOAuth2UserToUser( oAuth2User );
            UserDTO savedUser = signUpUser( newUserDto );
            log.debug("created new github oauth2 user {}", savedUser.getUserName());
            return buildJwt( userService.findById(savedUser.getId()).get() );
        }
    }


    /**
     * builds a JWT from Blogen User data. The JWT will build a "scope" claim containing the user's spring security
     * roles, plus an additional API scope, granting the user access to the Blogen API
     *
     * @param user - Blogen {@link User} data
     * @return - a JWT in compact form (BASE64 encoded)
     */
    private String buildJwt(User user) {
        List<String> scopes = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        scopes.add( BlogenAuthority.API.toString() );
        String tok = tokenService.builder().withScopes(scopes).withSubject( Long.toString(user.getId()) ).buildToken();
        return tok;
    }

    @Override
    public Boolean userNameExists( String userName ) {
        return userService.findByUserName( userName ).isPresent();
    }
}
