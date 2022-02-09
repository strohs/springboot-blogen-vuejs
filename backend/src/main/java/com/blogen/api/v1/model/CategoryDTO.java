package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object containing {@link com.blogen.domain.Category} data to be exposed to clients.
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    @Schema(description = "Category ID", accessMode = Schema.AccessMode.READ_ONLY, example = "236")
    private Long id;

    @Schema(description = "Category Name", required = true, example = "Business")
    private String name;

    @Schema(description = "URL of this Category", accessMode = Schema.AccessMode.READ_ONLY, example = "/api/v1/categories/4")
    private String categoryUrl;
}
