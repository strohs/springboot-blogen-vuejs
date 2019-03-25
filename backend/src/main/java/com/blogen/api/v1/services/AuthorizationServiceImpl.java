package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.LoginResponse;
import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.oauth2.OAuth2Providers;
import com.blogen.domain.Role;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.services.security.BlogenAuthority;
import com.blogen.services.security.EncryptionService;
import com.blogen.services.security.BlogenJwtService;
import lombok.extern.slf4j.Slf4j;
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
    private UserMapper userMapper;
    private EncryptionService encryptionService;
    private BlogenJwtService tokenService;

    private static final String DEFAULT_AVATAR_IMAGE = "avatar0.jpg";

    public AuthorizationServiceImpl(
            UserService userService,
            UserMapper userMapper,
            EncryptionService encryptionService,
            BlogenJwtService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
        this.tokenService = tokenService;
    }

    @Override
    public Boolean userNameExists( String userName ) {
        return userService.findByUserName( userName ).isPresent();
    }

    @Override
    public UserDTO signUpUser( UserDTO userDTO ) {
        try {
            User newUser = userService.createNewUser( userDTO );
            UserDTO returnDto = userMapper.userToUserDto( newUser );
            returnDto.setUserUrl( UserService.buildUserUrl( newUser ) );
            return returnDto;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException( "user with userName=" + userDTO.getUserName() + " already exists" );
        }
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

}
