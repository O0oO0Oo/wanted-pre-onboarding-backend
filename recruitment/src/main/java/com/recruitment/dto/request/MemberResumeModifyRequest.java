package com.recruitment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberResumeModifyRequest(
        @NotNull(message = "는 널값이면 안 됩니다.")
        Long memberResumeId,
        @NotNull(message = "는 널값이면 안 됩니다.")
        Long memberId,
        @NotBlank(message = "는 공백이거나 널값이면 안 됩니다.")
        String title,
        @NotBlank(message = "는 공백이거나 널값이면 안 됩니다.")
        String description
) {
}
