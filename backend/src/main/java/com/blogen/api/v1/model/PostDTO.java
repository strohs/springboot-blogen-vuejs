package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Blogen {@link com.blogen.domain.Post} data
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    @ApiModelProperty( value = "Post ID", readOnly = true, example = "42")
    private Long id;

    @ApiModelProperty( value = "title of the post", required = true, example = "Some Amazing Title")
    private String title;

    @ApiModelProperty( value = "text of the post", required = true, example = "text of the post")
    private String text;

    @ApiModelProperty( value = "url to an image on the web", example = "http://lorempixe/200/400/abstract")
    private String imageUrl;

    @ApiModelProperty( value = "category that this post belongs to", required = true, example = "Web Development")
    private String categoryName;

    @ApiModelProperty(value = "the userName of the user creating this post", required = true, example = "coolUserName")
    private String userName;

    @ApiModelProperty(value = "ISO8601 date of when this post was created" ,readOnly = true )
    private LocalDateTime created;

    @ApiModelProperty(value = "URL that identifies this post", readOnly = true, example = "/api/v1/posts/43")
    private String postUrl;

    @ApiModelProperty(value = "URL that identifies the parent of this post, will be null if the post is a parent", readOnly = true, example = "/api/v1/posts/40")
    private String parentPostUrl;

    @ApiModelProperty(value = "if this is a parent post, contains its child posts", readOnly = true)
    private List<PostDTO> children;
    

}
