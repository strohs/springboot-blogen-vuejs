package com.blogen.builders;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper methods for building various domain objects used in blogen tests
 *
 * @author Cliff
 */
public class Builder {

    public static User buildUser( Long id, String userName, String firstName, String lastName, String email, String password, String encryptedPassword ) {
        User user = new User();
        user.setId( id );
        user.setUserName( userName );
        user.setFirstName( firstName );
        user.setLastName( lastName );
        user.setEmail( email );
        user.setPassword( password );
        user.setEncryptedPassword( encryptedPassword );
        return user;
    }

    public static UserPrefs buildUserPrefs( Long id, String avatarName ) {
        UserPrefs prefs = new UserPrefs();
        prefs.setId( id );
        prefs.setAvatarImage( avatarName );
        return prefs;
    }

    public static List<String> buildAvatarImages( String ... names ) {
        return Arrays.stream( names ).collect( Collectors.toList());
    }

    public static Post buildPost( Long id, String title, String text, String image, Category cat, User user, Post parent ) {
        Post post = new Post();
        post.setId( id );
        post.setTitle( title );
        post.setText( text );
        post.setImageUrl( image );
        post.setCategory( cat );
        post.setUser( user );
        post.setParent( parent );
        return post;
    }
    

    public static Category buildCategory( Long id, String name ) {
        Category cat = new Category();
        cat.setId( id );
        cat.setName( name );
        return cat;
    }


    //Builders for REST API
    public static PostDTO buildPostDTO( Long id, String userName, String title, String text,
                                        String imageUrl, String categoryName, LocalDateTime created, List<PostDTO> children) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId( id );
        postDTO.setUserName( userName );
        postDTO.setCategoryName( categoryName );
        postDTO.setText( text );
        postDTO.setTitle( title );
        postDTO.setImageUrl( imageUrl );
        postDTO.setCreated( created );
        postDTO.setChildren( children );
        return postDTO;
    }

}
