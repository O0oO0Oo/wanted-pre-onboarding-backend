package com.recruitment.dto.mapping;

import java.time.ZonedDateTime;

/**
 * @@ to_tsquery 는 JPQL 에서 사용하지 못하기에 Record 타입인 JobPositionResponse와 매핑시켜주기 위해 만들었다
 * record 타입으로 native query 을 매핑하는 방법을 찾지 못해서 중간 매개자로 만듦.
 */
public interface MappingJobPostingResponse {
    Long getJobPostingId();
    String getCompanyName();
    Long getCompanyId();
    String getCountry();
    String getCity();
    String getJobPosition();
    Long getCompensation();
    String getSkills();
    ZonedDateTime getLastUpdate();
}