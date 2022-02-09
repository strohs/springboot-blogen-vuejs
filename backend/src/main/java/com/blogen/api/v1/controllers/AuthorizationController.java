package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.AuthorizationService;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.UserDtoSignupValidator;
import com.blogen.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for handling endpoints that do NOT require users to be authenticated and authorized,
 * i.e. new-user sign-ups, checking if a username exists,
 *
 */
@Tag(name = "Authorization", description = "operations for logging in users")
@Slf4j
@RestController
@RequestMapping(AuthorizationController.BASE_URL)
public class AuthorizationController {

    public static final String BASE_URL = "/api/v1/auth";

    private AuthorizationService authorizationService;
    private UserDtoSignupValidator userSignupValidator;
    private PostService postService;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService,
                                   UserDtoSignupValidator userSignupValidator,
                                   PostService postService) {
        this.authorizationService = authorizationService;
        this.userSignupValidator = userSignupValidator;
        this.postService = postService;
    }

    @InitBinder("userDTO")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(userSignupValidator);
    }


    @Operation(summary = "sign-up a new user")
    @PostMapping(value = "/signup", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signupUser(@RequestBody @Valid UserDTO userDTO) {
        log.debug("signup user userDTO=" + userDTO);
        return authorizationService.signUpUser(userDTO);
    }

    @Operation(summary = "get the latest posts")
    @GetMapping(value = "/latestPosts", produces = {"application/json"})
    public PostListDTO latestPosts(@RequestParam(name = "limit", defaultValue = "9") int limit) {
        log.debug("get latest posts limit={}", limit);
        return postService.getPosts(-1L, 0, limit);
    }

    @Operation(summary = "check if a user name exists")
    @GetMapping(value = "/username/{name}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void userNameExists(@PathVariable("name") String userName) {
        Boolean userExists = authorizationService.userNameExists(userName);
        log.debug("check user name exists: {} = {}", userName, userExists);
        if (!userExists) {
            throw new NotFoundException("user name not found: " + userName);
        }
    }
}
