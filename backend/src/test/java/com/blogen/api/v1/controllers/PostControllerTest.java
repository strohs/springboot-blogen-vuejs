package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.PostDtoValidator;
import com.blogen.builders.Builder;
import com.blogen.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.blogen.api.v1.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RestControllerTest using @WebMvcTest
 * @author Cliff
 */
@RunWith( SpringRunner.class )
@WebMvcTest(controllers = {PostController.class}, secure = false)
@Import( {PostDtoValidator.class} )
public class PostControllerTest {

    @MockBean
    PostService postService;

    @Autowired
    WebApplicationContext webCtx;

    @Autowired
    MockMvc mockMvc;

    PostDTO postDTO_1;
    PostDTO postDTO_2;
    PostDTO childDTO_1;

    @Before
    public void setUp() throws Exception {
        
        //build two parent posts and one child post. The child post will belong to postDTO_1
        childDTO_1 = Builder.buildPostDTO( 3L, "user","child title","child text",null,"cat1", LocalDateTime.now(),new ArrayList<>() );
        childDTO_1.setPostUrl( PostController.BASE_URL + "/" + "3" );
        childDTO_1.setParentPostUrl( PostController.BASE_URL + "/" + "1" );
        postDTO_1 = Builder.buildPostDTO( 1L,"user","post1 title","post1 text",null,"cat1", LocalDateTime.now(),new ArrayList<>() );
        postDTO_1.setPostUrl( PostController.BASE_URL + "/" + "1" );
        postDTO_1.getChildren().add( childDTO_1 );
        postDTO_2 = Builder.buildPostDTO( 2L,"user","post2 title","post2 text","http://images.com/2","cat1", LocalDateTime.now(),new ArrayList<>() );
        postDTO_2.setPostUrl( PostController.BASE_URL + "/" + "2" );
    }

    @Test
    public void should_getOneParentPostsAndReturnOK_when_getPostsWithLimitSet() throws Exception {
        PostListDTO postListDTO = new PostListDTO( Arrays.asList( postDTO_1 ) );

        given( postService.getPosts( anyInt() )).willReturn( postListDTO );

        mockMvc.perform( get( PostController.BASE_URL )
                            .contentType( MediaType.APPLICATION_JSON )
                            .requestAttr( "limit",1 ))
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.posts",hasSize(1) ) );
    }

    @Test
    public void should_getTwoParentPostsAndReturnOK_when_getPostsWithNoLimitSet() throws Exception {
        PostListDTO postListDTO = new PostListDTO( Arrays.asList( postDTO_1, postDTO_2) );

        given( postService.getPosts( anyInt() )).willReturn( postListDTO );

        mockMvc.perform( get( PostController.BASE_URL )
                .contentType( MediaType.APPLICATION_JSON ))
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.posts",hasSize(2) ) );
    }

    @Test
    public void should_getPostById_when_getPost() throws Exception {
        String requestUrl = PostController.BASE_URL + "/1";
        given( postService.getPost( anyLong() )).willReturn( postDTO_1 );

        mockMvc.perform( get( requestUrl )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.postUrl", is( requestUrl )) );
    }

    @Test
    public void should_returnHTTP_NOT_FOUND_when_getPostWithBadID() throws Exception {
        String requestUrl = PostController.BASE_URL + "/565";
        given( postService.getPost( anyLong() )).willThrow( NotFoundException.class );

        mockMvc.perform( get( requestUrl )
                .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
    }

    @Test
    public void should_returnCREATED_when_createNewPostWithValidContent() throws Exception {
        //"2018-01-30T12:39:53.798"
        //created and children should not be sent as part of request body
        postDTO_2.setCreated( null );
        postDTO_2.setChildren( null );
        postDTO_2.setPostUrl( null );
        given( postService.createNewPost( any(PostDTO.class) )).willReturn( postDTO_2 );

        mockMvc.perform( post( PostController.BASE_URL)
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( postDTO_2 ) ) )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.postUrl",is( postDTO_2.getPostUrl() ) ) );
    }

    @Test
    public void should_return_UNPROCESSABLE_ENTITY_when_createNewPostWithMissingField() throws Exception {
        //"2018-01-30T12:39:53.798"
        //created and children should not be sent as part of request body
        postDTO_2.setCreated( null );
        postDTO_2.setChildren( null );
        //userName is required
        postDTO_2.setUserName( null );
        given( postService.createNewPost( any(PostDTO.class) )).willReturn( postDTO_2 );

        mockMvc.perform( post( PostController.BASE_URL)
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( postDTO_2 ) ) )
                .andExpect( status().isUnprocessableEntity());
    }

    @Test
    public void should_returnCREATED_when_createNewChildPost_requestBodyContainsValidPostDTO() throws Exception {
        childDTO_1.setCreated( null );
        childDTO_1.setPostUrl( null );
        childDTO_1.setParentPostUrl( null );

        given( postService.createNewChildPost( anyLong(), any(PostDTO.class) )).willReturn( childDTO_1 );

        mockMvc.perform( post( PostController.BASE_URL + "/1")
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( childDTO_1 ) ) )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.title",is(childDTO_1.getTitle()) ) );
    }

    @Test
    public void should_returnUNPROCESSABLE_ENTITY_when_requestBodyContainsNullPostDtoField() throws Exception {
        childDTO_1.setCreated( null );
        childDTO_1.setCategoryName( null );

        given( postService.createNewChildPost( anyLong(), any(PostDTO.class) )).willReturn( childDTO_1 );

        mockMvc.perform( post( PostController.BASE_URL + "/1")
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( childDTO_1 ) ) )
                .andExpect( status().isUnprocessableEntity() );
    }

    @Test
    public void should_returnUNPROCESSABLE_ENTITY_when_requestBodyHas_RequiredFieldThatIsEmpty() throws Exception {
        childDTO_1.setCreated( null );
        //category is a required field
        childDTO_1.setCategoryName( "" );

        given( postService.createNewChildPost( anyLong(), any(PostDTO.class) )).willReturn( childDTO_1 );

        mockMvc.perform( post( PostController.BASE_URL + "/1" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( childDTO_1 ) ) )
                .andExpect( status().isUnprocessableEntity() );
    }

    @Test
    public void should_returnOK_when_updatePostWithValidRequestData() throws Exception {
        postDTO_2.setTitle( "new title" );
        postDTO_2.setCreated( null );
        postDTO_2.setPostUrl( null );
        postDTO_2.setParentPostUrl( null );

        given( postService.saveUpdatePost( anyLong(), any( PostDTO.class ) )).willReturn( postDTO_2 );

        mockMvc.perform( put( PostController.BASE_URL + "/2")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( asJsonString( postDTO_2 ) ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.title", is( "new title" ) ) );
    }

    @Test
    public void should_deletePost() throws Exception {
        mockMvc.perform( delete( PostController.BASE_URL + "/2" ))
                .andExpect( status().isOk() );
    }
}