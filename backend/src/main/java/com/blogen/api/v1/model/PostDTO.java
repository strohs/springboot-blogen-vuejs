package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Post ID", accessMode = Schema.AccessMode.READ_ONLY, example = "42")
    private Long id;

    @Schema(description = "title of the post", required = true, example = "Some Amazing Title")
    private String title;

    @Schema(description = "text of the post", required = true, example = "text of the post")
    private String text;

    @Schema(description = "url to an image on the web", example = "http://lorempixe/200/400/abstract")
    private String imageUrl;

    @Schema(description = "category that this post belongs to", required = true, example = "Web Development")
    private CategoryDTO category;

    @Schema(description = "the userName and id of the user who created this post", required = true, example = "coolUserName")
    private PostUserDTO user;

    @Schema(description = "ISO8601 date of when this post was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created;

    @Schema(description = "URL that identifies this post", accessMode = Schema.AccessMode.READ_ONLY, example = "/api/v1/posts/43")
    private String postUrl;

    @Schema(description = "URL that identifies the parent of this post, will be null if the post is a parent", accessMode = Schema.AccessMode.READ_ONLY, example = "/api/v1/posts/40")
    private String parentPostUrl;

    @Schema(description = "if this is a parent post, contains its child posts", accessMode = Schema.AccessMode.READ_ONLY)
    private List<PostDTO> children;

}
