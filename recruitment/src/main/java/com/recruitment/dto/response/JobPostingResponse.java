package com.recruitment.dto.response;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.dto.mapping.MappingJobPostingResponse;

import java.time.ZonedDateTime;

/**
 * 채용공고 목록 응답 Dto
 */
public record JobPostingResponse(
    Long jobPostingId,
    String companyName,
    Long companyId,
    String country,
    String city,
    String jobPosition,
    Long compensation,
    String skills,
    ZonedDateTime lastUpdate
) {

    public static JobPostingResponse fromEntities(JobPosting jobPosting, Company company) {
        return new JobPostingResponse(
                jobPosting.getId(),
                company.getCompanyName(),
                company.getId(),
                company.getCountry(),
                company.getCity(),
                jobPosting.getJobPosition(),
                jobPosting.getCompensation(),
                jobPosting.getSkills(),
                jobPosting.getLastUpdate()
        );
    }

    // native query 로 불러온 데이터와 매핑
    public static JobPostingResponse fromMappingDto(MappingJobPostingResponse mappingJobPostingResponse) {
        return new JobPostingResponse(
                mappingJobPostingResponse.getJobPostingId(),
                mappingJobPostingResponse.getCompanyName(),
                mappingJobPostingResponse.getCompanyId(),
                mappingJobPostingResponse.getCountry(),
                mappingJobPostingResponse.getCity(),
                mappingJobPostingResponse.getJobPosition(),
                mappingJobPostingResponse.getCompensation(),
                mappingJobPostingResponse.getSkills(),
                mappingJobPostingResponse.getLastUpdate()
        );
    }
}
