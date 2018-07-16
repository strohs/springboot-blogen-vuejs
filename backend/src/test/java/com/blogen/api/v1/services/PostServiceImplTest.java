package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.PostController;
import com.blogen.api.v1.mappers.PostMapper;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.exceptions.NotFoundException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
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
 * @author Cliff
 */
public class PostServiceImplTest {

    PostServiceImpl postService;

    @Mock
    PostRepository postRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PageRequestBuilder pageRequestBuilder;

    PostMapper postMapper  = PostMapper.INSTANCE;

    private static final Long   CAT1_ID   = 1L;
    private static final String CAT1_NAME = "Business";
    private static final Long   CAT2_ID   = 4L;
    private static final String CAT2_NAME = "Tech Gadgets";
    private static final Long   USER_ID   = 1L;
    private static final String USER_NAME = "johnny";
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
    private static final String POST1_URL = PostController.BASE_URL + "/" + POST1_ID;
    private static final String CHILD1_POST_URL = PostController.BASE_URL + "/" + CHILD1_ID;
    private static final String CHILD1_PARENT_POST_URL = PostController.BASE_URL + "/" + POST1_ID;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        postService = new PostServiceImpl( pageRequestBuilder, postRepository, categoryRepository, userRepository, postMapper );
    }

    @Test
    public void should_getOnePost_when_getPostSizeIsOne() {
        int size = 1;
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, null, POST1_TEXT, null, cat1, user1, null );
        List<Post> posts = new ArrayList<>();
        posts.add( post1 );
        PageRequest pageRequest = new PageRequest( 0,size,Sort.Direction.DESC,"category" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByParentNullOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );

        PostListDTO postDTOS = postService.getPosts( size );

        then( pageRequestBuilder ).should().buildPageRequest( anyInt(),anyInt(),any(Sort.Direction.class), anyString() );
        then( postRepository).should().findAllByParentNullOrderByCreatedDesc( any( Pageable.class ) );
        assertThat( postDTOS.getPosts().size(), is(1));
    }

    @Test
    @Ignore
    public void should_getTwoPosts_when_getPostsSizeIsLessThanEqualToZero() {
        int size = -1;
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, null, POST1_TEXT, null, cat1, user1, null );
        Post post2 = Builder.buildPost( POST2_ID, null, POST2_TEXT, null, cat1, user1, null );
        List<Post> posts = Arrays.asList( post1, post2 );
        PageRequest pageRequest = new PageRequest( 0,25,Sort.Direction.DESC,"category" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByParentNullOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass( Integer.class );

        PostListDTO postDTOS = postService.getPosts( size );

        verify( pageRequestBuilder ).buildPageRequest( anyInt(),argumentCaptor.capture(),any(Sort.Direction.class),anyString() );
        assertThat( argumentCaptor.getValue(), is(5) );
        then( postRepository).should().findAllByParentNullOrderByCreatedDesc( any( Pageable.class ) );
        assertThat( postDTOS.getPosts().size(), is(2));
    }

    @Test
    public void should_setPostUrlToPost1Url_when_getPostsReturnsParentPost() {
        int size = 5;
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, null, POST1_TEXT, null, cat1, user1, null );
        List<Post> posts = Arrays.asList( post1 );
        PageRequest pageRequest = new PageRequest( 0,25,Sort.Direction.DESC,"category" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByParentNullOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );

        PostListDTO postDTOS = postService.getPosts( size );

        then( postRepository ).should().findAllByParentNullOrderByCreatedDesc( any( Pageable.class ) );
        assertThat( postDTOS.getPosts().get( 0 ).getPostUrl(), is(POST1_URL));
        assertThat( postDTOS.getPosts().get( 0 ).getParentPostUrl(), is( nullValue()) );
    }

    @Test
    @Ignore
    public void should_setParentPostUrl_when_getPostsReturnsChildPost() {
        int size = 5;
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, null, POST1_TEXT, null, cat1, user1, null );
        Post child1 = Builder.buildPost( CHILD1_ID, null, CHILD1_TEXT, null, cat1, user1, post1 );
        post1.addChild( child1 );
        List<Post> posts = Arrays.asList( child1 );
        PageRequest pageRequest = new PageRequest( 0,10,Sort.Direction.DESC,"category" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByParentNullOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );

        PostListDTO postDTOS = postService.getPosts( size );

        assertThat( postDTOS.getPosts().get( 0 ).getPostUrl(), is(CHILD1_POST_URL) );
        assertThat( postDTOS.getPosts().get( 0 ).getParentPostUrl(), is( CHILD1_PARENT_POST_URL ));
    }

    @Test
    public void should_getOnePostById_when_getPost() {
        Post post1 = buildPost1();
        List<Post> posts = Arrays.asList( post1 );

        given( postRepository.findById(anyLong()) ).willReturn( Optional.of( post1 ) );

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
    public void should_createNewParentPost() {
        Post post1 = buildPost1();
        PostDTO dtoToSave = buildPost1DTO();

        given( categoryRepository.findByName( anyString() )).willReturn( post1.getCategory() );
        given( userRepository.findByUserName( anyString() )).willReturn( post1.getUser() );
        given( postRepository.save( any( Post.class ) )).willReturn( post1 );

        PostDTO postDTO = postService.createNewPost( dtoToSave );

        then( categoryRepository ).should().findByName( anyString() );
        then( userRepository ).should().findByUserName( anyString() );
        then( postRepository ).should().save( any( Post.class ) );
        assertThat( postDTO, is( notNullValue()) );
        assertThat( postDTO.getText(), is( dtoToSave.getText()) );
        assertThat( postDTO.getTitle(), is( dtoToSave.getTitle()) );
        assertThat( postDTO.getUserName(), is( dtoToSave.getUserName()) );
        assertThat( postDTO.getCategoryName(), is( dtoToSave.getCategoryName()) );
    }

    @Test(expected = BadRequestException.class )
    public void should_throwBadRequestException_when_postDtoRequestContainsUsernameThatDoesNotExist() {
        Post post1 = buildPost1();
        PostDTO dtoToSave = buildPost1DTO();
        dtoToSave.setUserName( "BAD_USERNAME" );

        given( userRepository.findByUserName( anyString() ) ).willReturn( null );

        PostDTO postDTO = postService.createNewPost( dtoToSave );
    }

    @Test(expected = BadRequestException.class )
    public void should_throwBadRequestException_when_postDtoRequestContainsCategoryNameThatDoesNotExist() {
        Post post1 = buildPost1();
        PostDTO dtoToSave = buildPost1DTO();
        dtoToSave.setCategoryName( "BAD_CATEGORY" );

        given( categoryRepository.findByName( anyString() ) ).willReturn( null );

        PostDTO postDTO = postService.createNewPost( dtoToSave );
    }

    @Test
    public void should_saveParentAndChild_AndSetURLs_when_createNewChildPost() {
        Post post1 = buildPost1();
        Post child1 = buildChild1();
        Post savedPost = buildPost1();
        savedPost.addChild( child1 );
        PostDTO childDTO = buildChild1DTO();

        given( postRepository.findById(anyLong()) ).willReturn( Optional.of( post1 ) );
        given( categoryRepository.findByName( anyString() )).willReturn( child1.getCategory() );
        given( userRepository.findByUserName( anyString() )).willReturn( child1.getUser() );
        given( postRepository.saveAndFlush( any(Post.class) )).willReturn( savedPost );

        PostDTO savedDTO = postService.createNewChildPost( POST1_ID, childDTO );

        then( postRepository ).should().findById(anyLong());
        then( categoryRepository ).should().findByName( anyString() );
        then( userRepository ).should().findByUserName( anyString() );
        then( postRepository ).should().saveAndFlush( any( Post.class ) );
        assertThat( savedDTO, is( notNullValue() ));
        assertThat( savedDTO.getChildren().size(), is( 1 ));
        assertThat( savedDTO.getPostUrl(), is(POST1_URL) );
        assertThat( savedDTO.getChildren().get( 0 ).getPostUrl(), is( CHILD1_POST_URL) );
        assertThat( savedDTO.getChildren().get( 0 ).getParentPostUrl(), is( CHILD1_PARENT_POST_URL) );
    }

    @Test(expected = BadRequestException.class)
    public void should_throwException_when_parentPostId_doesNotExist() {
        Post post1 = buildPost1();
        Post child1 = buildChild1();
        Post savedPost = buildPost1();
        savedPost.addChild( child1 );
        PostDTO childDTO = buildChild1DTO();

        given( postRepository.findById(anyLong()) ).willReturn( Optional.empty() );

        PostDTO savedDTO = postService.createNewChildPost( 45342L, childDTO );
    }

    @Test
    public void should_savePostDTO_when_saveUpdatePost() {
        Post post = buildPost1();
        Post savedPost = buildPost1();
        savedPost.setTitle( POST2_TITLE );
        PostDTO postDTO = buildPost1DTO();
        postDTO.setTitle( POST2_TITLE );

        given( postRepository.findById(anyLong()) ).willReturn( Optional.of( post ) );
        given( postRepository.save( any(Post.class) )).willReturn( savedPost );
        given( categoryRepository.findByName( anyString() )).willReturn( post.getCategory() );
        given( userRepository.findByUserName( anyString() )).willReturn( post.getUser() );

        PostDTO savedDTO = postService.saveUpdatePost( POST1_ID, postDTO );

        then( postRepository ).should().findById(anyLong());
        then( postRepository).should().save( any( Post.class ) );
        assertThat( savedDTO.getPostUrl(), is( POST1_URL) );
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
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, POST1_TITLE, POST1_TEXT, null, cat1, user1, null );
        return post1;
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
        return Builder.buildPostDTO( POST1_ID, USER_NAME, POST1_TITLE, POST1_TEXT, null, CAT1_NAME, LocalDateTime.now(), new ArrayList<>() );
    }
    private PostDTO buildChild1DTO() {
        return Builder.buildPostDTO( CHILD1_ID, USER_NAME, CHILD1_TITLE, CHILD1_TEXT, null, CAT1_NAME, LocalDateTime.now(), new ArrayList<>() );
    }
}