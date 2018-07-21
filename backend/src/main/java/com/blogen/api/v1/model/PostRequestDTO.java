package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object that encapsulates a request to create a new post
 * Author: Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {

    @ApiModelProperty( value = "title of the post", required = true, example = "Some Amazing Title")
    private String title;

    @ApiModelProperty( value = "text of the post", required = true, example = "text of the post")
    private String text;

    @ApiModelProperty( value = "url to an image on the web", example = "http://lorempixe/200/400/abstract")
    private String imageUrl;

    @ApiModelProperty( value = "category id that this post belongs to", required = true, example = "2")
    private Long categoryId;

}
