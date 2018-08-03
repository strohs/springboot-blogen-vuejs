package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.PostController;
import com.blogen.api.v1.mappers.PostMapper;
import com.blogen.api.v1.mappers.PostRequestMapper;
import com.blogen.api.v1.model.*;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.exceptions.NotFoundException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import com.blogen.services.AvatarService;
import com.blogen.services.PrincipalService;
import com.blogen.services.security.UserDetailsImpl;
import com.blogen.services.utils.PageRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for performing RESTful CRUD operations on Blogen {@link com.blogen.domain.Post}
 *
 * @author Cliff
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private PageRequestBuilder pageRequestBuilder;
    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserService userService;
    private AvatarService avatarService;
    private PostMapper postMapper;
    private PostRequestMapper postRequestMapper;
    private PrincipalService principalService;



    @Autowired
    public PostServiceImpl( PageRequestBuilder pageRequestBuilder, PostRepository postRepository,
                            CategoryRepository categoryRepository, UserService userService,
                            AvatarService avatarService, PostMapper postMapper,
                            PostRequestMapper postRequestMapper, PrincipalService principalService ) {
        this.pageRequestBuilder = pageRequestBuilder;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.avatarService = avatarService;
        this.postMapper = postMapper;
        this.postRequestMapper = postRequestMapper;
        this.principalService = principalService;
    }


    @Override
    public PostListDTO getPosts( Long categoryId, int pageNum, int pageSize ) {
        //check if category exists
        if( categoryId != null && categoryId > -1)
            validateCategoryId( categoryId );
        //create a PageRequest
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( pageNum, pageSize, Sort.Direction.DESC,"created" );
        //retrieve the posts
        Page<Post> page;
        if ( categoryId > -1 )
            page = postRepository.findAllByCategory_IdAndParentNull( categoryId, pageRequest );
        else
            page = postRepository.findAllByParentNullOrderByCreatedDesc( pageRequest );
        List<PostDTO> postDTOS = new ArrayList<>();
        page.forEach( post -> {
            PostDTO dto = buildReturnDto( post );
            postDTOS.add( dto );
        } );
        return new PostListDTO( postDTOS, PageRequestBuilder.buildPageInfoResponse(page) );
    }

    @Override
    public PostDTO getPost( Long id ) {
        Post post = postRepository.findById( id ).orElseThrow( () -> new NotFoundException( "post not found with id:" + id ) );
        return buildReturnDto( post );
    }


    @Override
    public PostListDTO getPostsForUser( Long userId, Long categoryId, int pageNum, int pageSize ) {
        //check if user exists and category exists
        validateUserId( userId );
        if (categoryId != null && categoryId > -1)
            validateCategoryId( categoryId );
        //create a PageRequest
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( pageNum, pageSize, Sort.Direction.DESC,"created" );
        Page<Post> page;
        if ( categoryId > -1 )
            page =  postRepository.findAllByUser_IdAndCategory_IdAndParentNull( userId, categoryId, pageRequest );
        else
            page = postRepository.findAllByUser_IdAndParentNull( userId, pageRequest );
        List<PostDTO> postDTOS = new ArrayList<>();
        page.forEach( post -> {
            PostDTO dto = buildReturnDto( post );
            postDTOS.add( dto );
        } );
        return new PostListDTO( postDTOS, PageRequestBuilder.buildPageInfoResponse(page) );
    }

    @Override
    @Transactional
    //creates a new Parent Post
    public PostDTO createNewPost( PostRequestDTO postDTO ) {
        Post post = buildNewPost( postDTO  );
        Post savedPost = postRepository.save( post );
        return buildReturnDto( savedPost );
    }

    @Override
    @Transactional
    public PostDTO createNewChildPost( Long parentId, PostRequestDTO requestDTO ) {
        Post parentPost = postRepository.findById( parentId ).orElseThrow( () ->
                new BadRequestException( "Post with id " + parentId + " was not found" ) );
        if ( !parentPost.isParentPost() )
            throw new BadRequestException( "Post with id: " + parentId + " is a child post. Cannot create a new child post onto an existing child post" );
        Post childPost = buildNewPost( requestDTO  );
        parentPost.addChild( childPost );
        Post savedPost = postRepository.saveAndFlush( parentPost );
        return buildReturnDto( savedPost );
    }

    @Override
    public PostDTO saveUpdatePost( Long id, PostRequestDTO requestDTO ) {
        Post postToUpdate = postRepository.findById( id ).orElseThrow( () ->
                new BadRequestException( "Post with id " + id + " was not found" ) );
        //TODO might be able to do this with POST mapper
        postToUpdate = mergePostRequestDtoToPost( postToUpdate, requestDTO );
        postToUpdate.setCreated( LocalDateTime.now() );
        Post savedPost = postRepository.save( postToUpdate );
        return buildReturnDto( savedPost );
    }

    @Override
    public PostListDTO searchPosts( String search, int limit ) {
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( 0, limit, Sort.Direction.DESC,"created" );
        Page<Post> page = postRepository.findByTextOrTitleIgnoreCaseContaining( search, pageRequest );
        List<PostDTO> postDTOS = new ArrayList<>();
        page.forEach( post -> {
            PostDTO dto = buildReturnDto( post );
            postDTOS.add( dto );
        } );
        return new PostListDTO( postDTOS, PageRequestBuilder.buildPageInfoResponse(page) );
    }

//    @Override
//    public PostDTO patchPost( Long id, PostDTO postDTO ) {
//        Post postToUpdate = postRepository.findOne( id );
//        if ( postToUpdate == null ) throw new NotFoundException( "Post with id " + id + " was not found" );
//        postToUpdate = postMapper.mergePostDtoToPost( postToUpdate, postDTO );
//        Post savedPost = postRepository.save( postToUpdate );
//        return buildReturnDto( savedPost );
//    }

    @Override
    @Transactional
    public void deletePost( Long id ) {
        Post post = postRepository.findById( id ).orElseThrow( () ->
                new BadRequestException( "Post with id " + id + " was not found" ) );
        if ( !post.isParentPost() ) {
            //post to delete is a child post, need to get the parent post object and remove the child from it.
            Post parent = post.getParent();
            parent.removeChild( post );
        }
        postRepository.delete( post );
    }

    /**
     * build a new {@link Post} object, making sure to retrieve existing data from the Category and User repositories.
     *
     * @param requestDTO
     * @return
     */
    private Post buildNewPost( PostRequestDTO requestDTO ) {
        // get the currently authenticated userName
        String userName = principalService.getPrincipalUserName();
        Post post = postRequestMapper.postRequestDtoToPost( requestDTO );
        post.setCreated( LocalDateTime.now() );
        Category category = categoryRepository.findById( requestDTO.getCategoryId() )
                .orElseThrow( () -> new BadRequestException( "Category does not exist with id:" + requestDTO.getCategoryId() ) );
        User user = userService.findByUserName( userName )
                .orElseThrow( () -> new BadRequestException( "User not found with name " + userName ) );
        post.setCategory( category );
        post.setUser( user );
        return post;
    }

    private String buildParentPostUrl( Post post ) {
        String url = null;
        //if the post is a child post, set the parent URL
        if ( !post.isParentPost() ) url = PostController.BASE_URL + "/" + post.getParent().getId();
        return url;
    }

    /**
     * build a PostDTO object and construct the URLs that get returned in the PostDTO
     * @param post - the Post domain object to convert into a PostDTO
     * @return a PostDTO
     */
    private PostDTO buildReturnDto( Post post ) {
        PostDTO postDTO = postMapper.postToPostDto( post );
        postDTO.setPostUrl( buildPostUrl( post ) );
        postDTO.getUser().setAvatarUrl( avatarService.buildAvatarUrl( post.getUser() ) );
        //if post is a child post, set the parentPostUrl
        if ( post.getParent() != null  ) postDTO.setParentPostUrl( buildPostUrl( post.getParent() ));
        if ( post.getChildren() != null ) {
            for ( int i = 0; i < post.getChildren().size(); i++ ) {
                PostDTO childDTO = postDTO.getChildren().get( i );
                Post child = post.getChildren().get( i );
                childDTO.setPostUrl( buildPostUrl( post.getChildren().get( i ) ) );
                childDTO.getUser().setAvatarUrl( avatarService.buildAvatarUrl( child.getUser() ) );
                childDTO.setParentPostUrl( buildParentPostUrl( child ) );
            }
        }
        return postDTO;
    }


    /**
     * Merge non-null fields of PostDTO into a {@link Post} object
     * @param target Post object to merge fields into
     * @param source PostRequestDTO containing the non-null fields you want to merge
     * @return a Post object containing the merged fields
     */
    private Post mergePostRequestDtoToPost( Post target, PostRequestDTO source ) {
        if ( source.getImageUrl() != null )
            target.setImageUrl( source.getImageUrl() );
        if ( source.getCategoryId() != null ) {
            Category category = validateCategoryId( source.getCategoryId() );
            target.setCategory( category );
        }
        if ( source.getTitle() != null )
            target.setTitle( source.getTitle() );
        if ( source.getText() != null )
            target.setText( source.getText() );
        return target;
    }

    /**
     * validate that a category name exists in the repository
     * @param name category name to search for
     * @return the Category corresponding to the name
     * @throws BadRequestException if the category name does not exist in the repository
     */
    private Category validateCategoryName( String name ) throws NotFoundException {
        Category category = categoryRepository.findByName( name );
        if ( category == null ) throw new NotFoundException( "category with name: " + name + " does not exist" );
        return category;
    }

    private Category validateCategoryId( Long id ) throws NotFoundException {
        return categoryRepository.findById( id )
                .orElseThrow( () -> new NotFoundException( "category with id: " + id + " does not exist" ) );
    }

    private User validateUserId( Long id ) throws NotFoundException {
        return userService.findById( id )
                .orElseThrow( () -> new NotFoundException( "user with id: " + id + " does not exist" ) );
    }

    /**
     * validate that a userName exists in the repository
     * @param name the username to search for
     * @return the {@link User} corresponding to the name
     * @throws BadRequestException if the username does not exist in the repository
     */
    private User validateUserName( String name ) throws BadRequestException {
        return userService.findByUserName( name )
                .orElseThrow( () -> new BadRequestException( "user with name: " + name + " does not exist" ) );
    }
}
