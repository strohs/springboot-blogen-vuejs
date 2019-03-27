package com.blogen.api.v1.services.oauth2;

import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.UserService;
import com.blogen.domain.User;
import com.blogen.services.security.BlogenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OAuth2UserLoginServiceImpl implements OAuth2UserLoginService {

    private UserService userService;
    private UserMapper userMapper;
    private BlogenJwtService jwtService;

    public OAuth2UserLoginServiceImpl(UserService userService, UserMapper userMapper, BlogenJwtService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @Override
    public String loginUser(final OAuth2Providers provider, final OAuth2User oAuth2User) {

        // users authenticated with OAuth2 providers have the providers name prefixed to the username
        String username = provider.toString().toLowerCase() + oAuth2User.getName();
        // check for user in our DB, if found return it as a UserDTO, else if not found, create a new user
        final UserDTO userDTO = userService
                .findByUserName(username)
                .map(userMapper::userToUserDto)
                .orElseGet( () -> createNewOauth2User( OAuth2UserMapper.getUserMapperForProvider(provider), oAuth2User ) );
        return jwtService.generateApiToken( userDTO.getId().toString(), userDTO.getRoles() );
    }

    protected UserDTO createNewOauth2User( OAuth2UserMapper mapper, OAuth2User oAuth2User) {
        User newUser = userService.createNewUser( mapper.mapUser( oAuth2User ) );
        log.debug("created new blogen user {} from oauth2 user {}", newUser.getUserName(), oAuth2User.getName());
        return userMapper.userToUserDto(newUser);
    }

}
