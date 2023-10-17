package com.recruitment.repository;

import com.recruitment.domain.MemberResume;
import com.recruitment.dto.response.MemberResumeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberResumeRepository extends JpaRepository<MemberResume, Long> {
    /**
     * member_id 와 page number 를 받아서 유저의 이력서 목록 페이징 처리, 이력서의 상세 내용은 50자만 출력
     */
    @Query(
            "SELECT new com.recruitment.dto.response.MemberResumeResponse(m.id ,m.title, SUBSTRING(m.description ,1 ,50))" +
                    " FROM MemberResume m WHERE m.member.id = :memberId ORDER BY m.lastUpdate DESC"
    )
    Page<MemberResumeResponse> findAllByMemberIdOrderByLast(Pageable pageable,@Param("memberId") Long memberId);

    Optional<MemberResume> findByIdAndMemberId(Long id, Long memberId);
}
