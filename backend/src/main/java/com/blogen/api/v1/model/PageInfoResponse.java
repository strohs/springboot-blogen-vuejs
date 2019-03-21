package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains information about a page of data. This object can be included in a response when API clients make requests
 * for a specific page
 *
 * Author: Cliff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageInfoResponse {

    @ApiModelProperty(value = "the total number of elements that exist when making a page request using the same pageNum and pageSize", readOnly = true)
    Long totalElements;

    @ApiModelProperty(value = "the total number of pages that exist when making a page request using the same pageNum and pageSize", readOnly = true)
    Integer totalPages;

    @ApiModelProperty(value = "echoes the page number that was requested, pages use 0 based indices", readOnly = true)
    Integer pageNumber;

    @ApiModelProperty(value = "echoes the page size (aka limit) that was requested", readOnly = true)
    Integer pageSize;
}
