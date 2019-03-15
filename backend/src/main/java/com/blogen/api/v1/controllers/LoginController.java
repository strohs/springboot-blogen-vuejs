package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.LoginRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.AuthorizationService;
import com.blogen.api.v1.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * handles logins from OAuth2 sources as well as logins where the client sends a JSON Web Token as their
 * credential (rather than a username and password)
 *
 */
@Slf4j
@Controller
@RequestMapping( LoginController.BASE_URL )
public class LoginController {

    public static final String BASE_URL = "/blogen/login";

    @Value("${vue.dev.server.port:${server.port:8080}}")
    private String redirectPort;

    private AuthorizationService authorizationService;
    private UserService userService;

    public LoginController(AuthorizationService authorizationService, UserService userService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @GetMapping("/userinfo")
    @ResponseBody
    // todo move this into Authentication controller
    public UserDTO getAuthenticatedUserInfo(@AuthenticationPrincipal Jwt jwt) {
        log.debug("get authenticated user info for :{}", jwt.getSubject() );
        return userService.getUser( Long.parseLong( jwt.getSubject() ));
    }

    @PostMapping( "/form" )
    public ResponseEntity<String> loginUsernameAndPassword( @RequestBody @Valid LoginRequestDTO loginDTO ) {
        log.debug( "login username:{} password:{}", loginDTO.getUsername(), loginDTO.getPassword() );

        String token = authorizationService.authenticateAndLoginUser( loginDTO.getUsername(), loginDTO.getPassword() );

        return ResponseEntity.ok()
                .header("Authorization","Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .build();
    }

    /**
     * logs a user into Blogen via redirect from the GitHub OAuth2 service
     * @param authorizedClient
     * @param oauth2User
     * @return
     * @throws IOException
     */
    @GetMapping("/github")
    public ResponseEntity<String> githubOAuth2Login(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletResponse response) throws IOException {
        //todo need to get authorization header from inside javascript, or put token into cookie
        log.debug("login via github for principal:{}",authorizedClient.getPrincipalName() );

        String token = authorizationService.loginGithubUser( oauth2User );
        response.addCookie( new Cookie("blogen-jwt", token));
        return buildTokenResponse(token);
    }


    /**
     * builds a ResponseEntity containing the main blogen index.html page in the body, plus an Authorization
     * header containing the Blogen bearer token in JWT format
     * @param bearerToken - the jwt to include in the response header (in compact format)
     * @return a ResponseEntity
     * @throws IOException
     */
    private ResponseEntity<String> buildTokenResponse(String bearerToken) throws IOException {
        File f = new ClassPathResource("public/index.html").getFile();
        String content = new String ( Files.readAllBytes( Paths.get( f.getPath() ) ) );

//        URI redirectUri = builder
//                .path("/login")
//                .port(redirectPort)
//                .encode().build().toUri();

        return ResponseEntity.ok()
                .header("Authorization","Bearer " + bearerToken)
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }
}
