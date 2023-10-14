package com.recruitment.dto.request;

/**
 * 채용공고 등록, 수정 Dto
 */
public record JobPostingAddRequest(
        Long companyId,
        String jobPostingDetails,
        String title,
        String jobPosition,
        Long compensation,
        String skills
        ) {
}


