package com.recruitment.dto.response;

/**
 * 채용공고 페이징 메타데이터 Dto
 */
public record PagingMetaData(
        int currentPage,
        int totalPages,
        long totalItems,
        int itemsPerPage
) {

}
