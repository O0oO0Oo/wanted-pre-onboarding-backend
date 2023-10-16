package com.recruitment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

/**
 * 채용공고 수정 요청 Dto
 */
public record JobPostingModifyRequest(
        @NotNull(message = "는 널값이면 안됩니다.")
        Long jobPostingId,
        @NotBlank(message = "는 공백이거나 널값이면 안됩니다.")
        String jobPostingDetails,
        @NotBlank(message = "는 공백이거나 널값이면 안됩니다.")
        String title,
        @NotBlank(message = "는 공백이거나 널값이면 안됩니다.")
        String jobPosition,
        @Range(min = 0, message = "는 0이상의 값을 입력해야 합니다.")
        @NotNull(message = "는 널값이면 안됩니다.")
        Long compensation,
        @NotBlank(message = "는 공백이거나 널값이면 안됩니다.")
        String skills
) {
}
