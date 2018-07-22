package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.PostController;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.model.PostRequestDTO;
import com.blogen.domain.Post;

/**
 * Service interface for REST methods that operate on Blogen {@link com.blogen.domain.Post}(s)
 *
 * @author Cliff
 */
public interface PostService {


    /**
     * get all posts containing the specified categoryId, for the specified pageNum, with  up to pageSize posts
     * per page
     * @param categoryId categoryId of posts to retrieve, set to -1 to get all posts in all categories
     * @param pageNum the page number of posts to retrieve
     * @param pageSize the number of posts, per page, to retrieve.
     * @return
     */
    PostListDTO getPosts( Long categoryId, int pageNum, int pageSize );

    /**
     * get a specific post by its ID
     *
     * @param id The id of the post to retrieve. If the id refers to a parent post, then the parent post and its
     *           children will be returned. If the ID refers to a child post, then only that child post will be returned
     * @return
     */
    PostDTO getPost( Long id );

    /**
     * get posts for the specified user and category
     * @param userId
     * @param categoryId the categoryId of the posts to retrieve
     * @param pageNum the page number of posts to return
     * @param limit max posts per page to return
     * @return
     */
    PostListDTO getPostsForUser( Long userId, Long categoryId, int pageNum, int limit );


    /**
     * Creates a new Parent Post. Any PostDTO.children sent will be ignored.
     *
     * @param requestDTO contains post data to create.
     * @return a {@link PostDTO} representing the newly created post
     */
    PostDTO createNewPost( PostRequestDTO requestDTO );

    /**
     * Creates a new child post. The child post will be associated with the Parent Post represented by the parentId
     * @param parentId id of the parent post, under which this child will be created
     * @return a {@link PostDTO} representing the newly created child post
     */
    PostDTO createNewChildPost( Long parentId, PostRequestDTO requestDTO );

    /**
     * Saves/updates an existing Post
     * @param id the id of the Post to update
     * @param requestDTO data to update the post with
     * @return a {@link PostDTO} containing the newly updated fields
     */
    PostDTO saveUpdatePost( Long id, PostRequestDTO requestDTO );

    /**
     * search posts title and text for the specified searchStr
     * @param search text string to search for
     * @return
     */
    PostListDTO searchPosts( String search, int limit );

    /**
     * updates specific fields of a Post
     * @param id id of the Post to update
     * @param postDTO contains the fields to update. Only fields you want to update should be set
     * @return a {@link PostDTO} containing the newly updated post data
     */
    //PostDTO patchPost( Long id, PostDTO postDTO );

    /**
     * Delete the post with the parentId
     * @param id ID of the post to delete
     */
    void deletePost( Long id );

    //helper method that builds a URL String to a particular post
    default String buildPostUrl( Post post ) {
        return PostController.BASE_URL + "/" + post.getId();
    }
}
