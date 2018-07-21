package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostUserDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Unit Test for MapStruct mapping between Post and PostDTO
 * @author Cliff
 */
public class PostMapperTest {

    private static final Long    CAT1_ID = 10L;
    private static final Long    CAT2_ID = 12L;
    private static final String  CAT1_NAME = "Health & Wellness";
    private static final String  CAT2_NAME = "Business";

    private static final Long    USER1_ID = 20L;
    private static final String  USER1_USERNAME = "jdoe";
    private static final String  USER1_EMAIL = "jdoe@gmail.com";
    private static final Long    USER2_ID = 22L;
    private static final String  USER2_USERNAME = "wallace";
    private static final String  USER2_EMAIL = "wally@gmail.com";

    private static final Long     POST_ID = 33L;
    private static final Long     PARENT_POST_ID = 1L;
    private static final LocalDateTime PARENT_POST_CREATED = LocalDateTime.of( 2017,1,1,10,10,10 );
    private static final String   PARENT_POST_CREATED_FORMAT = "Sun Jan 01, 2017 10:10 AM";
    private static final String   PARENT_POST_TEXT = "sample text for blogen";
    private static final String   PARENT_POST_TITLE = "Title for Post";
    private static final String   PARENT_POST_IMAGE_URL = "http://pexels.com/technics/400/200/1";


    private static final Long    CHILD1_POST_ID   = 5L;
    private static final String  CHILD1_POST_TEXT = "sample child text";
    private static final String  CHILD1_POST_TITLE = "Child Title";
    private static final String  CHILD1_POST_IMAGE_URL = null;
    private static final LocalDateTime CHILD1_POST_CREATED = LocalDateTime.of( 2017,1,1,10,15,15 );
    private static final String  CHILD1_POST_CREATED_FORMAT = "Sun Jan 01, 2017 10:15 AM";


    private PostMapper postMapper = PostMapper.INSTANCE;

    private Post parent;

    @Before
    public void setUp() throws Exception {
        Category cat1 = new Category();
        cat1.setId( CAT1_ID );
        cat1.setName( CAT1_NAME );

        Category cat2 = new Category();
        cat2.setId( CAT2_ID );
        cat2.setName( CAT2_NAME );

        User user1 = new User();
        user1.setId( USER1_ID );
        user1.setUserName( USER1_USERNAME );
        user1.setEmail( USER1_EMAIL );

        User user2 = new User();
        user2.setId( USER2_ID );
        user2.setUserName( USER2_USERNAME );
        user2.setEmail( USER1_EMAIL );

        parent = new Post();
        parent.setId( PARENT_POST_ID );
        parent.setCategory( cat1 );
        parent.setUser( user1 );
        parent.setParent( null );
        parent.setText( PARENT_POST_TEXT );
        parent.setImageUrl( PARENT_POST_IMAGE_URL );
        parent.setCreated( PARENT_POST_CREATED );

        Post child1 = new Post();
        child1.setId( CHILD1_POST_ID );
        child1.setText( CHILD1_POST_TEXT );
        child1.setImageUrl( CHILD1_POST_IMAGE_URL );
        child1.setCreated( CHILD1_POST_CREATED );
        child1.setCategory( cat1 );
        child1.setUser( user2 );

        parent.addChild( child1 );

    }

    @Test
    public void should_mapAllProperties_when_postToPostDto() {

        //when
        PostDTO postDTO = postMapper.postToPostDto( parent );

        //then
        assertNotNull( postDTO );

        assertThat( postDTO.getId(), is(PARENT_POST_ID) );
        assertThat( postDTO.getCategory().getName(), is(CAT1_NAME) );
        assertThat( postDTO.getText(), is( PARENT_POST_TEXT) );
        assertThat( postDTO.getImageUrl(), is( PARENT_POST_IMAGE_URL) );
        assertThat( postDTO.getCreated(), is( PARENT_POST_CREATED) );

        assertThat( postDTO.getUser().getUserName(), is( USER1_USERNAME) );
        assertThat( postDTO.getChildren().size(), is( 1 ) );

        //test child posts
        PostDTO child1 = postDTO.getChildren().get( 0 );

        assertThat( child1.getId(), is(CHILD1_POST_ID) );
        assertThat( child1.getCreated(), is(CHILD1_POST_CREATED) );
        assertThat( child1.getText(), is( CHILD1_POST_TEXT) );
        assertThat( child1.getImageUrl(), is( nullValue()) );

        assertThat( child1.getUser().getUserName(), is( USER2_USERNAME) );

        assertThat( child1.getCategory().getName(), is(CAT1_NAME) );
        assertThat( child1.getChildren().size(), is(0) );

    }

    @Test
    public void should_mapPostDTOtoPost_when_postDTOtoPost() {
        //given
        PostUserDTO postUserDTO = PostUserDTO.builder().id( USER1_ID ).userName( USER1_USERNAME ).build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id( CAT1_ID ).name( CAT1_NAME ).build();
        PostDTO postDTO = Builder.buildPostDTO( PARENT_POST_ID, postUserDTO, PARENT_POST_TITLE,
                PARENT_POST_TEXT, PARENT_POST_IMAGE_URL,categoryDTO,PARENT_POST_CREATED, new ArrayList<>( ) );
        //when
        Post post = postMapper.postDtoToPost( postDTO );

        //then
        assertNotNull( postDTO );
        assertThat( post.getId(), is(PARENT_POST_ID) );
        assertThat( post.getUser().getUserName(), is(USER1_USERNAME) );
        assertThat( post.getTitle(), is(PARENT_POST_TITLE));
        assertThat( post.getText(), is( PARENT_POST_TEXT) );
        assertThat( post.getImageUrl(), is(PARENT_POST_IMAGE_URL) );
        assertThat( post.getCategory().getName(), is( CAT1_NAME) );
        assertThat( post.getCreated(), is( PARENT_POST_CREATED) );
        assertThat( post.getChildren().size(), is(0));
        assertThat( post.getUser().getEmail(), is( nullValue()) );
    }

    @Test
    public void should_mapPostDTOChild_To_PostChild_when_postDTOtoPost() {
        //given
        PostUserDTO postUserDTO = PostUserDTO.builder().id( USER1_ID ).userName( USER1_USERNAME ).build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id( CAT1_ID ).name( CAT1_NAME ).build();

        PostDTO childDTO = Builder.buildPostDTO( CHILD1_POST_ID, postUserDTO,CHILD1_POST_TITLE,
                CHILD1_POST_TEXT, CHILD1_POST_IMAGE_URL,categoryDTO,CHILD1_POST_CREATED, new ArrayList<>( ) );
        List<PostDTO> children = Arrays.asList( childDTO );
        PostDTO postDTO = Builder.buildPostDTO( PARENT_POST_ID, postUserDTO,PARENT_POST_TITLE,
                PARENT_POST_TEXT, PARENT_POST_IMAGE_URL,categoryDTO,PARENT_POST_CREATED, children );
        //when
        Post post = postMapper.postDtoToPost( postDTO );

        //then
        assertNotNull( postDTO );
        assertThat( post.getChildren().size(), is(1));
        assertThat( post.getChildren().get( 0 ).getId(), is( CHILD1_POST_ID ) );
        assertThat( post.getChildren().get( 0 ).getTitle(), is( CHILD1_POST_TITLE ) );
        assertThat( post.getChildren().get( 0 ).getText(), is( CHILD1_POST_TEXT ) );
        assertThat( post.getChildren().get( 0 ).getChildren().size(), is( 0) );
        
    }

//    @Test
//    public void should_onlyUpdateNonNullDtoFields_when_updatePostFromDTO() {
//        Category category = Builder.buildCategory( CAT1_ID, CAT1_NAME );
//        User user = Builder.buildUser( USER1_ID,USER1_USERNAME,"first","last","email","","secret" );
//        Post post = Builder.buildPost( PARENT_POST_ID, PARENT_POST_TITLE, PARENT_POST_TEXT, null, category, user, null  );
//        PostDTO postDTO = Builder.buildPostDTO( PARENT_POST_ID, USER1_USERNAME, null, "NEW TEXT","NEW URL", "NEW CAT", LocalDateTime.now(), new ArrayList<>() );
//
//        post = postMapper.updatePostFromDTO( postDTO,post );
//
//        assertThat( post.getId(), is( PARENT_POST_ID) );
//        assertThat( post.getTitle(), is( nullValue() ) );
//        assertThat( post.getText(), is("NEW TEXT") );
//        assertThat( post.getImageUrl(), is("NEW URL") );
//        assertThat( post.getCategory().getName(), is( "NEW CAT") );
//        assertThat( post.getCategory().getId(), is( CAT1_ID) );
//    }

}