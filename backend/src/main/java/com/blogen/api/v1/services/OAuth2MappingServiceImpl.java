package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.OAuth2UserMapper;
import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OAuth2MappingServiceImpl implements OAuth2MappingService {


    private UserService userService;
    private OAuth2UserMapper oAuth2UserMapper;
    private UserMapper userMapper;

    public OAuth2MappingServiceImpl(UserService userService, OAuth2UserMapper oAuth2UserMapper, UserMapper userMapper) {
        this.userService = userService;
        this.oAuth2UserMapper = oAuth2UserMapper;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO mapUser(OAuth2Provider provider, OAuth2User oAuth2User) {
        User user;

        // check if this OAuth user already has an account with Blogen
        String username = provider.toString().toLowerCase() + oAuth2User.getName();
        Optional<User> userOpt = userService.findByUserName(username);
        if ( userOpt.isPresent() ) {
            log.debug("oauth2 user exists: {}, logging them in and generating token", username);
            user = userOpt.get();
        } else {
            // create a new user
            UserDTO newUserDto;
            if ( provider == OAuth2Provider.GITHUB )
                newUserDto = oAuth2UserMapper.githubOAuth2UserToUserDTO( oAuth2User );
            else
                newUserDto = oAuth2UserMapper.googleOAuth2UserToUserDTO( oAuth2User );
            user = userService.createNewUser( newUserDto );
            log.debug("created new blogen user {} from oauth2 user {}", user.getUserName(), oAuth2User.getName() );
        }
        return userMapper.userToUserDto(user);
    }

}
