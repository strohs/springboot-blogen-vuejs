package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "container for categoryDTO", accessMode = Schema.AccessMode.READ_ONLY)
    List<CategoryDTO> categories;

    @Schema(description = "page information", accessMode = Schema.AccessMode.READ_ONLY)
    PageInfoResponse pageInfo;
}
