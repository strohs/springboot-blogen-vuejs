package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.PostRequestDTO;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.PostRequestDtoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Post", description = "operations on Blogen posts")
@Slf4j
@RestController
@RequestMapping(PostController.BASE_URL)
//TODO possibly add securitySchema annotations to all methods
public class PostController {

    public static final String BASE_URL = "/api/v1/posts";

    private PostService postService;
    private PostRequestDtoValidator postRequestDtoValidator;

    @Autowired
    public PostController(PostRequestDtoValidator postRequestDtoValidator, PostService postService) {
        this.postService = postService;
        this.postRequestDtoValidator = postRequestDtoValidator;
    }

    @InitBinder("postRequestDTO")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(postRequestDtoValidator);
    }

    @Operation(summary = "get a list of parent posts and any child posts belonging to a parent")
    @GetMapping(produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public PostListDTO getPosts(@RequestParam(value = "limit", defaultValue = "5") int limit,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "category", defaultValue = "-1") Long category) {
        log.debug("getPosts page={} limit={} category={}", page, limit, category);
        return postService.getPosts(category, page, limit);
    }

    @Operation(summary = "search posts for the passed in text")
    @GetMapping(value = "/search/{text}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public PostListDTO searchPosts(@PathVariable(value = "text") String text,
                                   @RequestParam(value = "limit", defaultValue = "5") int limit) {
        log.debug("search posts limit={} search text:{}", limit, text);
        return postService.searchPosts(text, limit);
    }

    @Operation(summary = "get a post by id")
    @GetMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public PostDTO getPost(@PathVariable("id") Long id) {
        log.debug("gePost id=" + id);
        return postService.getPost(id);
    }

    @Operation(summary = "create a new parent post")
    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO createPost(@Valid @RequestBody PostRequestDTO dto) {
        log.debug("createPost: " + dto);
        return postService.createNewPost(dto);
    }

    @Operation(summary = "create a new child post")
    @PostMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO createChildPost(@PathVariable("id") Long parentId, @Valid @RequestBody PostRequestDTO postRequestDTO) {
        log.debug("createChildPost id=" + parentId + "\n" + postRequestDTO);
        return postService.createNewChildPost(parentId, postRequestDTO);
    }

    @Operation(summary = "replace an existing post with new post data")
    @PutMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public PostDTO updatePost(@PathVariable("id") Long id, @Valid @RequestBody PostRequestDTO postRequestDTO) {
        log.debug("updatePost id=" + id + " postDTO:\n" + postRequestDTO);
        return postService.saveUpdatePost(id, postRequestDTO);
    }

    @Operation(summary = "update field(s) of an existing post")
    @PatchMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public PostDTO patchPost(@PathVariable("id") Long id, @RequestBody PostRequestDTO postRequestDTO) {
        log.debug("patchPost id=" + id + "\n" + postRequestDTO);
        return postService.saveUpdatePost(id, postRequestDTO);
    }

    @Operation(summary = "delete a post")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("id") Long id) {
        log.debug("deletePost id=" + id);
        postService.deletePost(id);
    }
}
