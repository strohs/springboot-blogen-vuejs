package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper object used to hold a list of {@link PostDTO}
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class PostListDTO {

    @Schema(description = "container for postDTO", accessMode = Schema.AccessMode.READ_ONLY)
    List<PostDTO> posts;

    @Schema(description = "container for a PageInfoResponse", accessMode = Schema.AccessMode.READ_ONLY)
    PageInfoResponse pageInfo;
}
