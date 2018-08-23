package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.PostRequestDTO;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.PostRequestDtoValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for working with {@link com.blogen.domain.Post}
 *
 * @author Cliff
 */
@Api
@Slf4j
@RestController
@RequestMapping( PostController.BASE_URL )
public class PostController {

    public static final String BASE_URL = "/api/v1/posts";

    private PostService postService;
    private PostRequestDtoValidator postRequestDtoValidator;

    @Autowired
    public PostController( PostRequestDtoValidator postRequestDtoValidator, PostService postService ) {
        this.postService = postService;
        this.postRequestDtoValidator = postRequestDtoValidator;
    }

    @InitBinder("postRequestDTO")
    public void setupBinder( WebDataBinder binder ) {
        binder.addValidators( postRequestDtoValidator );
    }

    @ApiOperation( value = "get a list of parent posts and any child posts belonging to a parent", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PostListDTO getPosts( @RequestParam(value = "limit", defaultValue = "5") int limit,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "category", defaultValue = "-1") Long category) {
        log.debug( "getPosts page={} limit={} category={}", page, limit, category );
        return postService.getPosts( category, page, limit );
    }

    @ApiOperation( value = "search posts for the passed in text", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @GetMapping( "/search/{text}" )
    @ResponseStatus(HttpStatus.OK)
    public PostListDTO searchPosts( @PathVariable(value = "text") String text,
                                    @RequestParam(value = "limit", defaultValue = "5") int limit) {
        log.debug( "search posts limit={} search text:{}", limit, text );
        return postService.searchPosts( text,limit );
    }

    @ApiOperation( value = "get a post by id", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @GetMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public PostDTO getPost( @PathVariable("id") Long id) {
        log.debug( "gePost id=" + id );
        return postService.getPost( id );
    }

    @ApiOperation( value = "create a new parent post", consumes = "application/json", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public PostDTO createPost( @Valid @RequestBody PostRequestDTO dto ) {
        log.debug( "createPost: " + dto );
        return postService.createNewPost( dto );
    }

    @ApiOperation( value = "create a new child post", consumes = "application/json", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @PostMapping( "/{id}")
    @ResponseStatus( HttpStatus.CREATED )
    public PostDTO createChildPost( @PathVariable("id") Long parentId, @Valid @RequestBody PostRequestDTO postRequestDTO ) {
        log.debug( "createChildPost id=" + parentId + "\n" + postRequestDTO );
        return postService.createNewChildPost( parentId, postRequestDTO );
    }

    @ApiOperation( value = "replace an existing post with new post data", consumes = "application/json", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @PutMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public PostDTO updatePost( @PathVariable("id") Long id, @Valid @RequestBody PostRequestDTO postRequestDTO ) {
        log.debug( "updatePost id=" + id + " postDTO:\n" + postRequestDTO );
        return postService.saveUpdatePost( id, postRequestDTO );
    }

    @ApiOperation( value = "update field(s) of an existing post", consumes = "application/json", produces = "application/json", authorizations = { @Authorization(value="apiKey") })
    @PatchMapping( "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public PostDTO patchPost( @PathVariable( "id" ) Long id, @RequestBody PostRequestDTO postRequestDTO ) {
        log.debug( "patchPost id=" + id + "\n" + postRequestDTO );
        return postService.saveUpdatePost( id, postRequestDTO );
    }

    @ApiOperation( value = "delete a post", authorizations = { @Authorization(value="apiKey") } )
    @DeleteMapping( "/{id}")
    @ResponseStatus( HttpStatus.OK )
    public void deletePost( @PathVariable("id") Long id ) {
        log.debug( "deletePost id=" + id );
        postService.deletePost( id );
    }
}
