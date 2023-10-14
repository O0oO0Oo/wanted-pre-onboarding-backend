package com.recruitment.dto.response;

import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;

/**
 * 채용 공고 상세 내용 Dto
 */
public record JobPostingModifyResponse(
        Long jobPostingId,
        String jobPostingDetails,
        String title,
        String jobPosition,
        Long compensation,
        String skills
) {
    public static JobPostingModifyResponse fromEntity(JobPosting jobPosting, JobPostingDetails jobPostingDetails1) {
        return new JobPostingModifyResponse(
                jobPosting.getId(),
                jobPostingDetails1.getDescription(),
                jobPosting.getTitle(),
                jobPosting.getJobPosition(),
                jobPosting.getCompensation(),
                jobPosting.getSkills()
        );
    }
}
