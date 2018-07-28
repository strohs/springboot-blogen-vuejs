package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDTO {

    @ApiModelProperty(value = "container for categoryDTO", readOnly = true)
    List<CategoryDTO> categories;

    @ApiModelProperty(value = "page information", readOnly = true)
    PageInfoResponse pageInfo;
}
