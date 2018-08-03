package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.PostRequestDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit Test mapping between Post and PostRequestDTO using the PostRequestMapper
 *
 * Author: Cliff
 */
public class PostRequestMapperTest {

    private PostRequestMapper mapper = PostRequestMapper.INSTANCE;

    Post post;

    PostRequestDTO postRequestDTO;

    @Before
    public void setUp() throws Exception {
        // set up a Post and a PostRequestDTO with all fields filled in
        Avatar avatar1 = Avatar.builder().id( 1L ).fileName( "avatar1.jpg" ).build();
        UserPrefs pref1 = UserPrefs.builder().id( 1L ).avatar( avatar1 ).build();
        User user1 = Builder.buildUser( 1L, "john11","John","Doe",
                "doe@gmail.com","password","df335sddfsf");
        user1.setUserPrefs( pref1 );
        Category cat1 = Category.builder().id( 1L ).name( "Business" ).build();

        // build a complete post with user,category etc...
        post = Builder.buildPost( 1L, "title","text","http://image.to",cat1, user1, null );

        //build a complete postRequestDTO
        postRequestDTO = PostRequestDTO.builder().categoryId( 2L ).imageUrl( "http://picsum.com" )
                .text( "new text" ).title( "new title" ).build();
    }

    @Test
    public void shouldMapAllPostRequestDTOfields_fromPost_intoPostRequestDto() {

        //when
        postRequestDTO = mapper.postToPostRequestDto( post );

        //then
        assertThat( postRequestDTO.getCategoryId(), is(post.getCategory().getId()) );
        assertThat( postRequestDTO.getImageUrl(), is(post.getImageUrl()) );
        assertThat( postRequestDTO.getText(), is(post.getText()) );
        assertThat( postRequestDTO.getTitle(), is(post.getTitle()) );
    }

    @Test
    public void shouldMapNonNullFieldsOfPostRequestDTO_intoPost_when_RequestDtoToPost() {
        // given a valid PostRequestDTO with all fields filled in and an existing Post

        //when
        post = mapper.postRequestDtoToPost( postRequestDTO );

        //then
        assertThat( post.getImageUrl(), is(postRequestDTO.getImageUrl()) );
        // category id is not mapped into Post
        assertThat( post.getCategory().getId(), is(postRequestDTO.getCategoryId()) );
        assertThat( post.getText(), is(postRequestDTO.getText()) );
        assertThat( post.getTitle(), is(postRequestDTO.getTitle()) );
    }

    @Test
    public void shouldNotMapNullFieldsFromRequestDTOintoPost_when_updatePostFromPostRequestDTO() {
        //given a PostRequestDTO with a null imageURL
        postRequestDTO.setImageUrl( null );

        //when
        mapper.updatePostFromPostRequestDTO( postRequestDTO, post );

        //then post.imageUrl should have its original value
        assertThat( post.getImageUrl(), is("http://image.to"));
    }
}