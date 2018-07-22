package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public UserController( UserService userService,
                           PostService postService ) {
        this.userService = userService;
        this.postService = postService;
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
    @PatchMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public UserDTO updateUser( @PathVariable("id") Long id, @RequestBody UserDTO userDTO ) {
        log.debug( "update user id=" + id + " userDTO=" + userDTO );
        return userService.updateUser( id, userDTO );
    }
}
