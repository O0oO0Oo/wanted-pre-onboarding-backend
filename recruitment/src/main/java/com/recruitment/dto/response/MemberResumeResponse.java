package com.recruitment.dto.response;

import com.recruitment.domain.MemberResume;

public record MemberResumeResponse(
        Long memberResumeId,
        String title,
        String description
) {
    public static MemberResumeResponse fromEntity(MemberResume memberResume) {
        return new MemberResumeResponse(
                memberResume.getId(),
                memberResume.getTitle(),
                memberResume.getDescription()
        );
    }
}
