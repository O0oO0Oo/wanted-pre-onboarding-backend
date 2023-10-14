package com.recruitment.dto.response;

public record PagingMetaData(
        int currentPage,
        int totalPages,
        long totalItems,
        int itemsPerPage
) {

}
