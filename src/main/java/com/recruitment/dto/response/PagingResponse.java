package com.recruitment.dto.response;

import java.util.List;

/**
 * 페이징 데이터 응답 Dto
 * @param <T> 페이징 할 데이터
 * @param metaData 페이징 메타데이터
 */
public record PagingResponse<T>(
        List<T> data,
        PagingMetaData metaData
) {
}
