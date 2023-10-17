package com.recruitment.dto.response;

import com.recruitment.domain.JobPosting;
import com.recruitment.domain.MemberResume;

public record JobApplicationHistoryResponse(
        /**
         * job_posting
         * - job_posting_id
         * - title
         * - position
         * - skills
         *
         * member_resume
         * - member_resume_id
         * - title
         * - description (50자 제한)
         */
        Long jobPostingId,
        String jobPostingTitle,
        String position,
        String skills,
        Long memberResumeId,
        String memberResumeTitle,
        String description
) {
    public static JobApplicationHistoryResponse fromEntities(JobPosting jobPosting, MemberResume memberResume) {
        return new JobApplicationHistoryResponse(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getJobPosition(),
                jobPosting.getSkills(),
                memberResume.getId(),
                memberResume.getTitle(),
                memberResume.getDescription()
        );
    }
}
