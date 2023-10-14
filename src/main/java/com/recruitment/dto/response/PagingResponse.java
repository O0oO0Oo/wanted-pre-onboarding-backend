package com.recruitment.dto.response;

import java.util.List;

public record PagingResponse<T>(
        List<T> data,
        PagingMetaData metaData
) {
}
