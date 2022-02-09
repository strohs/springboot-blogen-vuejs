package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains information about a page of data. This object can be included in a response when API clients make requests
 * for a specific page
 * <p>
 * Author: Cliff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageInfoResponse {

    @Schema(description = "the total number of elements that exist when making a page request using the same pageNum and pageSize", accessMode = Schema.AccessMode.READ_ONLY)
    Long totalElements;

    @Schema(description = "the total number of pages that exist when making a page request using the same pageNum and pageSize", accessMode = Schema.AccessMode.READ_ONLY)
    Integer totalPages;

    @Schema(description = "echoes the page number that was requested, pages use 0 based indices", accessMode = Schema.AccessMode.READ_ONLY)
    Integer pageNumber;

    @Schema(description = "echoes the page size (aka limit) that was requested", accessMode = Schema.AccessMode.READ_ONLY)
    Integer pageSize;
}
