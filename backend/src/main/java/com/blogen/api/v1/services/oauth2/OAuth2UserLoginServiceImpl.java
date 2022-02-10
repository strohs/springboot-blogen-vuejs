package com.blogen.api.v1.services.oauth2;

import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.UserService;
import com.blogen.domain.User;
import com.blogen.services.security.BlogenAuthority;
import com.blogen.services.security.BlogenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OAuth2UserLoginServiceImpl implements OAuth2UserLoginService {

    private UserService userService;
    private UserMapper userMapper;
    private BlogenJwtService jwtService;

    // list of Oath2 Providers that blogen supports
    private final List<String> authorizedProviders = List.of("GOOGLE", "GITHUB");

    public OAuth2UserLoginServiceImpl(UserService userService, UserMapper userMapper, BlogenJwtService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @Override
    public String loginUser(final OAuth2Providers provider, final OAuth2User oAuth2User) {

        // map the OAuth2User data into a blogen userDTO object
        UserDTO userDTO = OAuth2UserMapper.getUserMapperForProvider(provider).mapUser(oAuth2User);

        // check for existing user in our DB, if found return it as a UserDTO, else if not found, create a new user
        // using credentials from the OAuth2User.
        userDTO = userService
                .findByUserName(userDTO.getUserName())
                .map(userMapper::userToUserDto)
                .orElseGet(() -> createNewOauth2User(OAuth2UserMapper.getUserMapperForProvider(provider), oAuth2User));

        // convert the user's roles, into BlogenAuthorities so we can generate our own JWT
        List<BlogenAuthority> roles = userDTO.getRoles()
                .stream()
                .map((role) -> BlogenAuthority.valueOf(role.toUpperCase()))
                .collect(Collectors.toList());

        return jwtService.generateToken(userDTO.getId().toString(), roles, null, null);
    }

    /**
     * creates a new UserDTO from a spring security OAuth2User object by mapping fields from oAuth2User
     * into a UserDTO.
     * <p>
     * The user fields that are required by Blogen are mapped using the provided OAuth2UserMapper
     *
     * @param mapper     a mapper object that maps required OAuth2User fields to UserDTO fields
     * @param oAuth2User a spring security OAuth2User object
     * @return a UserDTO containing user info provided by the oAuth2User
     */
    protected UserDTO createNewOauth2User(OAuth2UserMapper mapper, OAuth2User oAuth2User) {
        User newUser = userService.createNewUser(mapper.mapUser(oAuth2User));
        log.debug("created new blogen user {} from oauth2 user {}", newUser.getUserName(), oAuth2User.getName());
        return userMapper.userToUserDto(newUser);
    }

}
