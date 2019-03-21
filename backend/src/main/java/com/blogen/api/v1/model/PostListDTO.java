package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "container for postDTO", readOnly = true)
    List<PostDTO> posts;

    @ApiModelProperty(value = "container for a PageInfoResponse", readOnly = true)
    PageInfoResponse pageInfo;
}
