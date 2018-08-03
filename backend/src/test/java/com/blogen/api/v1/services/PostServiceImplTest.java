package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.CategoryController;
import com.blogen.api.v1.controllers.PostController;
import com.blogen.api.v1.controllers.UserController;
import com.blogen.api.v1.mappers.PostMapper;
import com.blogen.api.v1.mappers.PostRequestMapper;
import com.blogen.api.v1.model.*;
import com.blogen.builders.Builder;
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
import com.blogen.services.utils.PageRequestBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

/**
 * Unit Tests for PostServiceImpl
 * 
 * @author Cliff
 */
public class PostServiceImplTest {

    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @Mock
    private AvatarService avatarService;

    @Mock
    private PageRequestBuilder pageRequestBuilder;

    @Mock
    private PrincipalService principalService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRequestMapper postRequestMapper;

    private static final Long   CAT1_ID   = 1L;
    private static final String CAT1_NAME = "Business";
    private static final String CAT1_URL = CategoryController.BASE_URL + "/" + CAT1_ID;
    private static final Long   CAT2_ID   = 4L;
    private static final String CAT2_NAME = "Tech Gadgets";
    private static final Long   USER_ID   = 1L;
    private static final String USER_NAME = "johnny";
    private static final String USER_URL = UserController.BASE_URL + "/" + USER_ID;
    private static final Long   USER2_ID   = 122L;
    private static final String USER2_NAME = "maggie";
    private static final Long   POST1_ID = 5L;
    private static final String POST1_TITLE = "post1 title";
    private static final String POST1_TEXT = "post1 text";
    private static final Long   POST2_ID = 10L;
    private static final String POST2_TITLE = "post2 title";
    private static final String POST2_TEXT = "post2 text";
    private static final Long   POST3_ID = 15L;
    private static final String POST3_TEXT = "post3 text";
    private static final Long   CHILD1_ID = 6L;
    private static final String CHILD1_TITLE = "child1 title";
    private static final String CHILD1_TEXT = "child1 text";
    private static final String POST1_IMAGE_URL = "http://image.to/111";
    private static final String POST1_URL = PostController.BASE_URL + "/" + POST1_ID;
    private static final String CHILD1_POST_URL = PostController.BASE_URL + "/" + CHILD1_ID;
    private static final String CHILD1_PARENT_POST_URL = PostController.BASE_URL + "/" + POST1_ID;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        postService = new PostServiceImpl( pageRequestBuilder, postRepository, categoryRepository,
                userService, avatarService, postMapper, postRequestMapper, principalService );
    }

    @Test
    public void should_buildPostUrl_when_givenPostWithID() {
        Post post = buildPost1();
        assertThat( postService.buildPostUrl( post ), is( POST1_URL) );
    }

    @Test
    public void should_getOnePost_when_pageSizeIsOne() {
        int pageSize = 1; int pageNum = 0;
        Post post1 = buildPost1();
        PostDTO post1DTO = buildPost1DTO();
        List<Post> posts = new ArrayList<>();
        posts.add( post1 );
        PageRequest pageRequest = PageRequest.of( pageNum,pageSize,Sort.Direction.DESC,"created" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString())).willReturn( pageRequest );
        given( postRepository.findAllByParentNullOrderByCreatedDesc( pageRequest )).willReturn( page );
        given( postMapper.postToPostDto( post1 )).willReturn( post1DTO );

        PostListDTO postDTOS = postService.getPosts(-1L, pageNum, pageSize );

        then( pageRequestBuilder ).should().buildPageRequest( pageNum, pageSize, Sort.Direction.DESC, "created" );
        then( postRepository).should().findAllByParentNullOrderByCreatedDesc( pageRequest );
        assertThat( postDTOS.getPosts().size(), is(1));
    }


    @Test
    public void should_setReturnedPostUrlToPost1Url_when_getPostsReturnsParentPost() {
        int pageSize = 25; int pageNum = 0;
        Post post1 = buildPost1();
        PostDTO postDTO1 = buildPost1DTO();
        List<Post> posts = Arrays.asList( post1 );
        PageRequest pageRequest = PageRequest.of( pageNum,pageSize,Sort.Direction.DESC,"created" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByParentNullOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );
        given( postMapper.postToPostDto( post1 )).willReturn( postDTO1 );

        PostListDTO postDTOS = postService.getPosts(-1L,pageNum, pageSize);

        then( postRepository ).should().findAllByParentNullOrderByCreatedDesc( pageRequest );
        assertThat( postDTOS.getPosts().get( 0 ).getPostUrl(), is( POST1_URL ));
        assertThat( postDTOS.getPosts().get( 0 ).getParentPostUrl(), is( nullValue()) );
    }
    

    @Test
    public void should_getOnePostById_when_getPost() {
        Post post1 = buildPost1();
        PostDTO post1DTO = buildPost1DTO();

        given( postRepository.findById( POST1_ID )).willReturn( Optional.of( post1 ) );
        given( postMapper.postToPostDto( post1 )).willReturn( post1DTO );

        PostDTO postDTO = postService.getPost( POST1_ID );

        then( postRepository ).should().findById( POST1_ID );
        assertThat( postDTO, is( notNullValue()) );
        assertThat( postDTO.getText(), is(POST1_TEXT) );
    }

    @Test(expected = NotFoundException.class )
    public void should_throwNotFoundExceptin_when_getPostWithNonExistantId() {
        Post post1 = buildPost1();
        List<Post> posts = Arrays.asList( post1 );

        given( postRepository.findById(anyLong()) ).willReturn( Optional.empty() );

        PostDTO postDTO = postService.getPost( 5583L );
    }

    @Test
    public void should_createNewParentPost_whenGivenValidRequestDTO() {
        Post post1 = buildPost1();
        PostDTO postDTO1 = buildPost1DTO();
        PostRequestDTO requestDTO = buildPostRequest1DTO();
        String principalName = USER_NAME;

        given( categoryRepository.findById( requestDTO.getCategoryId() )).willReturn( Optional.of(post1.getCategory() ));
        given( userService.findByUserName( principalName )).willReturn( Optional.of( post1.getUser() ));
        given( postRepository.save( post1 )).willReturn( post1 );
        given( principalService.getPrincipalUserName() ).willReturn( USER_NAME );
        given( postMapper.postToPostDto( post1 )).willReturn( postDTO1 );
        given( postRequestMapper.postRequestDtoToPost( requestDTO ) ).willReturn( post1 );


        PostDTO postDTO = postService.createNewPost( requestDTO );

        then( categoryRepository ).should().findById( requestDTO.getCategoryId() );
        then( userService ).should().findByUserName( principalName );
        then( postRepository ).should().save( post1 );
        assertThat( postDTO, is( notNullValue()) );
        assertThat( postDTO.getText(), is( requestDTO.getText()) );
        assertThat( postDTO.getTitle(), is( requestDTO.getTitle()) );
        assertThat( postDTO.getUser().getUserName(), is( principalName ) );
        assertThat( postDTO.getCategory().getId(), is( requestDTO.getCategoryId() ) );
    }

    @Test(expected = BadRequestException.class )
    public void should_throwBadRequestException_when_postRequestDtoContainsCategoryIdThatDoesNotExist() {
        PostRequestDTO requestDTO = buildPostRequest1DTO();
        Post post1 = buildPost1();
        //categoryID does not exist
        requestDTO.setCategoryId( 999L );
        String principalName = USER_NAME;

        given( categoryRepository.findById( requestDTO.getCategoryId() ) ).willReturn( Optional.empty() );
        given( userService.findByUserName( principalName )).willReturn( Optional.of(post1.getUser() ));
        given( postRequestMapper.postRequestDtoToPost( requestDTO ) ).willReturn( post1 );

        postService.createNewPost( requestDTO );
    }

    @Test(expected = BadRequestException.class )
    public void should_throwBadRequestException_when_principalNameIsNull() {
        PostRequestDTO requestDTO = buildPostRequest1DTO();
        Post post1 = buildPost1();
        String principalName = null;

        given( categoryRepository.findById( anyLong() ) ).willReturn( Optional.of( post1.getCategory()) );
        given( userService.findByUserName( principalName )).willReturn( Optional.empty() );
        given( postRequestMapper.postRequestDtoToPost( requestDTO ) ).willReturn( post1 );

        postService.createNewPost( requestDTO );
    }

    @Test
    public void should_saveParentAndChild_AndSetURLs_when_createNewChildPost() {
        PostRequestDTO requestDTO = buildPostRequest1DTO(); // request to create a child post
        Post parentPost1 = buildPost1();
        Post child1 = buildChild1();
        Post savedPost = buildPost1();
        savedPost.addChild( child1 );
        PostDTO parentPostDTO = buildPost1DTO();
        PostDTO childDTO = buildChild1DTO();
        PostDTO savedPostDTO = buildPost1DTO();
        savedPostDTO.setChildren( Arrays.asList( childDTO ) );
        String principalName = USER_NAME;

        given( postRepository.findById( POST1_ID ) ).willReturn( Optional.of( parentPost1 ) );
        given( categoryRepository.findById( requestDTO.getCategoryId() )).willReturn( Optional.of(child1.getCategory()) );
        given( principalService.getPrincipalUserName() ).willReturn( principalName );
        given( userService.findByUserName( principalName )).willReturn( Optional.of( child1.getUser() ));
        given( postRequestMapper.postRequestDtoToPost( requestDTO ) ).willReturn( child1 );
        given( postMapper.postToPostDto( parentPost1 )).willReturn( parentPostDTO );
        given( postMapper.postToPostDto( child1 )).willReturn( childDTO );
        given( postMapper.postToPostDto( savedPost )).willReturn( savedPostDTO );
        given( postRepository.saveAndFlush( parentPost1 )).willReturn( savedPost );

        PostDTO savedDTO = postService.createNewChildPost( POST1_ID, requestDTO );

        then( postRepository ).should().findById( POST1_ID );
        then( categoryRepository ).should().findById( requestDTO.getCategoryId() );
        then( userService ).should().findByUserName( principalName );
        then( postRepository ).should().saveAndFlush( parentPost1 );
        assertThat( savedDTO, is( notNullValue() ));
        assertThat( savedDTO.getChildren().size(), is( 1 ));
        assertThat( savedDTO.getPostUrl(), is(POST1_URL) );
        assertThat( savedDTO.getChildren().get( 0 ).getPostUrl(), is( CHILD1_POST_URL) );
        assertThat( savedDTO.getChildren().get( 0 ).getParentPostUrl(), is( CHILD1_PARENT_POST_URL) );
    }

    @Test(expected = BadRequestException.class)
    public void should_throwException_when_parentPostId_doesNotExist() {
        PostRequestDTO request1DTO = buildPostRequest1DTO();
        Long parentID = 45342L;

        given( postRepository.findById( parentID) ).willReturn( Optional.empty() );

        PostDTO savedDTO = postService.createNewChildPost( parentID, request1DTO );
    }

    @Test
    public void should_savePostDTO_when_saveUpdatePost() {
        PostRequestDTO requestDTO = buildPostRequest1DTO();
        requestDTO.setTitle( POST2_TITLE );
        Post post = buildPost1();
        Post savedPost = buildPost1();
        savedPost.setTitle( POST2_TITLE );
        PostDTO savedPostDTO = buildPost1DTO();
        savedPostDTO.setTitle( POST2_TITLE );
        String principalName = USER_NAME;

        given( postRepository.findById( post.getId() )).willReturn( Optional.of( post ) );
        given( postRepository.save( post )).willReturn( savedPost );
        given( categoryRepository.findById( requestDTO.getCategoryId() )).willReturn( Optional.of( post.getCategory() ));
        given( postRequestMapper.postRequestDtoToPost( requestDTO ) ).willReturn( post );
        given( postMapper.postToPostDto( savedPost )).willReturn( savedPostDTO );
        given( principalService.getPrincipalUserName() ).willReturn( principalName );
        //given( userRepository.findByUserName( anyString() )).willReturn( post.getUser() );

        PostDTO savedDTO = postService.saveUpdatePost( POST1_ID, requestDTO );

        then( postRepository ).should().findById( post.getId() );
        then( postRepository).should().save( post );
        then( categoryRepository ).should().findById( requestDTO.getCategoryId() );
        assertThat( savedDTO.getTitle(), is( POST2_TITLE ) );
        assertThat( savedDTO.getPostUrl(), is( POST1_URL ) );
    }

//    @Test
//    @Ignore
//    public void should_patchPost_when_patchPost() {
//        Post post = buildPost1();
//        Post savedPost = buildPost1();
//        savedPost.setTitle( POST2_TITLE );
//        PostDTO postDTO = buildPost1DTO();
//        postDTO.setTitle( POST2_TITLE );
//
//        given( postRepository.findById(anyLong()).get()).willReturn( post );
//        given( postRepository.save( any(Post.class) )).willReturn( savedPost );
//
//        PostDTO savedDTO = postService.saveUpdatePost( POST1_ID, postDTO );
//
//        then( postRepository ).should().findById(anyLong()).get();
//        then( postRepository).should().save( any( Post.class ) );
//        assertThat( savedDTO.getPostUrl(), is( POST1_URL) );
//    }

    @Test
    public void deletePost() {
        Post post1 = buildPost1();

        given( postRepository.findById( POST1_ID ) ).willReturn( Optional.of(post1) );

        postService.deletePost( POST1_ID );

        then( postRepository ).should().delete( any( Post.class ) );
    }


    private Post buildPost1() {
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, "first" , "last","email@example.com","password","secret"  );
        Post post1 = Builder.buildPost( POST1_ID, POST1_TITLE, POST1_TEXT, POST1_IMAGE_URL, cat1, user1, null );
        return post1;
    }

    private PostRequestDTO buildPostRequest1DTO() {
        return PostRequestDTO.builder().title( POST1_TITLE ).text( POST1_TEXT ).categoryId( CAT1_ID ).imageUrl( POST1_IMAGE_URL ).build();
    }

    private Post buildChild1() {
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, POST1_TITLE, POST1_TEXT, null, cat1, user1, null );
        Post child1 = Builder.buildPost( CHILD1_ID, CHILD1_TITLE, CHILD1_TEXT, null, cat1, user1, post1 );
        post1.addChild( child1 );
        return child1;
    }

    private PostDTO buildPost1DTO() {
        PostUserDTO postUserDTO = PostUserDTO.builder().id( USER_ID ).userName( USER_NAME ).userUrl( USER_URL ).build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id( CAT1_ID ).name( CAT1_NAME ).categoryUrl( CAT1_URL ).build();
        return Builder.buildPostDTO( POST1_ID, postUserDTO, POST1_TITLE, POST1_TEXT, POST1_IMAGE_URL, categoryDTO, LocalDateTime.now(), new ArrayList<>() );
    }
    private PostDTO buildChild1DTO() {
        PostUserDTO postUserDTO = PostUserDTO.builder().id( USER_ID ).userName( USER_NAME ).userUrl( USER_URL ).build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id( CAT1_ID ).name( CAT1_NAME ).categoryUrl( CAT1_URL ).build();
        return Builder.buildPostDTO( CHILD1_ID, postUserDTO, CHILD1_TITLE, CHILD1_TEXT, POST1_IMAGE_URL, categoryDTO, LocalDateTime.now(), new ArrayList<>() );
    }
}