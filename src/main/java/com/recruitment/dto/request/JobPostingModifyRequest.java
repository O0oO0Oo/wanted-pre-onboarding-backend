package com.recruitment.dto.request;

public record JobPostingModifyRequest(
        Long jobPostingId,
        String jobPostingDetails,
        String title,
        String jobPosition,
        Long compensation,
        String skills
) {
}
