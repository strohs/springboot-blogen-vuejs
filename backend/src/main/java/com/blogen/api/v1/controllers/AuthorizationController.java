package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.AuthorizationService;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.UserDtoSignupValidator;
import com.blogen.exceptions.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for handling endpoints that do NOT require users to be authenticated and authorized,
 * i.e. new-user sign-ups and user log-ins
 *
 * Author: Cliff
 */
@Api
@Slf4j
@RestController
@RequestMapping( AuthorizationController.BASE_URL )
public class AuthorizationController {

    public static final String BASE_URL = "/api/v1/auth";

    private AuthorizationService authorizationService;
    private UserDtoSignupValidator userSignupValidator;
    private PostService postService;

    @Autowired
    public AuthorizationController( AuthorizationService authorizationService,
                                    UserDtoSignupValidator userSignupValidator,
                                    PostService postService ) {
        this.authorizationService = authorizationService;
        this.userSignupValidator = userSignupValidator;
        this.postService = postService;
    }

    @InitBinder("userDTO")
    public void setupBinder( WebDataBinder binder ) {
        binder.addValidators( userSignupValidator );
    }


    @ApiOperation( value = "sign-up a new user", consumes = "application/json", produces = "application/json")
    @PostMapping( "/signup" )
    @ResponseStatus( HttpStatus.CREATED )
    public UserDTO signupUser( @RequestBody @Valid UserDTO userDTO ) {
        log.debug( "signup user userDTO=" + userDTO );
        return authorizationService.signUpUser( userDTO );
    }

    @ApiOperation( value = "get the latest posts", produces = "application/json")
    @GetMapping( "/latestPosts" )
    public PostListDTO latestPosts( @RequestParam( name = "limit", defaultValue = "9") int limit ) {
        log.debug( "get latest posts limit={}", limit );
        return postService.getPosts( -1L, 0, limit );
    }

    @ApiOperation( value = "check if a user name exists, return HTTP Status 200 if it does, else returns 404", produces = "application/json")
    @GetMapping(value = "/username/{name}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void userNameExists( @PathVariable("name") String userName ) {
        Boolean userExists = authorizationService.userNameExists( userName );
        log.debug( "check user name exists: {} = {}", userName, userExists );
        if ( !userExists ) {
            throw new NotFoundException( "user name not found: " + userName );
        }
    }
}
