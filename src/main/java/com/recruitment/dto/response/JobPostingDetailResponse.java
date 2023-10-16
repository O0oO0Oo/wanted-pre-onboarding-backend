package com.recruitment.dto.response;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;

import java.util.List;

/**
 * 채용공고 상세 내용 응답 Dto
 */
public record JobPostingDetailResponse(
        Long jobPostingId,
        String companyName,
        String country,
        String city,
        String jobPosition,
        Long compensation,
        String skills,
        String details,
        List<Long> sameCompanyJobPostingIds
) {
    public static JobPostingDetailResponse fromEntities(JobPosting jobPosting, JobPostingDetails jobPostingDetails, Company company, List<Long> sameCompanyJobPostingIds) {
        return new JobPostingDetailResponse(
                jobPosting.getId(),
                company.getCompanyName(),
                company.getCountry(),
                company.getCity(),
                jobPosting.getJobPosition(),
                jobPosting.getCompensation(),
                jobPosting.getSkills(),
                jobPostingDetails.getDescription(),
                sameCompanyJobPostingIds
        );
    }
}
