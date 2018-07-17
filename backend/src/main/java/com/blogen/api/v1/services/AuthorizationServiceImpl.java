package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.domain.Role;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.services.RoleService;
import com.blogen.services.security.EncryptionService;
import com.blogen.services.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
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
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    private static final String DEFAULT_AVATAR_IMAGE = "avatar0.jpg";

    @Autowired
    public AuthorizationServiceImpl( UserService userService, RoleService roleService,
                                     UserMapper userMapper, EncryptionService encryptionService,
                                     AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider ) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public UserDTO signUpUser( UserDTO userDTO ) {
        //required fields validated in controller
        if (userDTO.getAvatarImage() == null ) userDTO.setAvatarImage( DEFAULT_AVATAR_IMAGE );
        Role userRole = roleService.getByName( "USER" );
        User user = userMapper.userDtoToUser( userDTO );
        user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
        user.addRole( userRole );
        User savedUser;
        try {
            savedUser = userService.saveUser( user );
        } catch ( DataIntegrityViolationException ex ) {
            throw new BadRequestException( "user with userName=" + userDTO.getUserName() + " already exists" );
        }
        UserDTO returnDto = userMapper.userToUserDto( savedUser );
        returnDto.setUserUrl( userService.buildUserUrl( savedUser ) );
        return returnDto;
    }

    @Override
    public String authenticateAndLoginUser( LoginRequestDTO loginDTO ) {
        String jwt = null;
        try {
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken( loginDTO.getUsername(), loginDTO.getPassword() );
            //authenticate username and password with authentication manager
            Authentication auth = authenticationManager.authenticate( userPassAuthToken );
            SecurityContextHolder.getContext().setAuthentication( auth );
            //if username and password are authenticated, generate and return a JSON Web Token
            jwt = tokenProvider.generateToken( auth );
        } catch (BadCredentialsException bce ) {
            throw new BadCredentialsException( "bad username or password" );
        }
        return jwt;
    }

}
