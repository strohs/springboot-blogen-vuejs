package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PasswordRequestDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.services.UserService;
import com.blogen.api.v1.validators.PasswordValidator;
import com.blogen.api.v1.validators.UpdateUserValidator;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for REST operations in Blogen {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
@Api
@Slf4j
@RestController
@RequestMapping( UserController.BASE_URL )
public class UserController {

    public static final String BASE_URL = "/api/v1/users";

    private UserService userService;
    private PostService postService;
    private UpdateUserValidator updateUserValidator;
    private PasswordValidator passwordValidator;

    @Autowired
    public UserController( UserService userService,
                           PostService postService,
                           UpdateUserValidator updateUserValidator,
                           PasswordValidator passwordValidator ) {
        this.userService = userService;
        this.postService = postService;
        this.updateUserValidator = updateUserValidator;
        this.passwordValidator = passwordValidator;
    }

    @InitBinder("userDTO")
    public void setupUpdateUserBinder( WebDataBinder binder ) {
        binder.addValidators( updateUserValidator );
    }

    @InitBinder("passwordRequestDTO")
    public void setupPasswordBinder( WebDataBinder binder ) {
        binder.addValidators( passwordValidator );
    }

    @ApiOperation( value = "get a list of all users", produces = "application/json")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserListDTO getAllUsers() {
        log.debug( "getAllUsers" );
        return userService.getAllUsers();
    }

    @ApiOperation( value = "get a specific user by id", produces = "application/json")
    @GetMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public UserDTO getUser( @PathVariable("id") Long id ) {
        log.debug( "getUser id=" + id );
        return userService.getUser( id );
    }

    @ApiOperation( value = "get posts made by a user", produces = "application/json")
    @GetMapping( "/{id}/posts" )
    @ResponseStatus( HttpStatus.OK )
    public PostListDTO getUserPosts( @PathVariable("id") Long id,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "limit", defaultValue = "5") Integer limit,
                                     @RequestParam(value = "category", defaultValue = "-1") Long category) {
        log.debug( "getUserPosts id={} page={} limit={} category={}", id, page, limit, category );
        // category =-1L indicates ignore category and get posts for any category
        return postService.getPostsForUser( id, category, page, limit );
    }

    @ApiOperation( value = "update field(s) of an existing user", consumes = "application/json", produces = "application/json")
    @PutMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public UserDTO updateUser( @PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO ) {
        log.debug( "update user id=" + id + " userDTO=" + userDTO );
        User user = userService.findById( id )
                .orElseThrow( () -> new BadRequestException( "user does not exist with id:" + id ) );
        return userService.updateUser( user, userDTO );
    }

    @ApiOperation( value = "change a users password", consumes = "application/json")
    @PutMapping( "/{id}/password" )
    @ResponseStatus( HttpStatus.OK )
    public void updatePassword( @PathVariable("id") Long id, @Valid @RequestBody PasswordRequestDTO passwordRequestDTO ) {
        log.debug( "change password user id=" + id + " passwordDTO= " + passwordRequestDTO );
        User user = userService.findById( id )
                .orElseThrow( () ->  new BadRequestException( "user does not exist with id:" + id ) );
        userService.changePassword( user, passwordRequestDTO );
    }
}
