package com.recruitment.repository;

import com.recruitment.domain.JobApplicationHistory;
import com.recruitment.dto.response.JobApplicationHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobApplicationHistoryRepository extends JpaRepository<JobApplicationHistory, Long> {
    /**
     * 사용자를 위한 지원 기록 조회, 50자 제한, 이력서 상세한 내용은 상세조회
     */
    @Query(
            "SELECT new com.recruitment.dto.response.JobApplicationHistoryResponse(" +
                    "j.jobPosting.id,j.jobPosting.title,j.jobPosting.jobPosition,j.jobPosting.skills," +
                    "j.memberResume.id,j.memberResume.title,SUBSTRING(j.memberResume.description, 1, 50)) " +
                    "FROM JobApplicationHistory j " +
                    "WHERE j.member.id = :memberId " +
                    "ORDER BY j.lastUpdate DESC"
    )
    Page<JobApplicationHistoryResponse> findAllByMemberIdOrderByLastForMember(Pageable pageable,
                                                                              @Param("memberId") Long memberId);

    /**
     * 회사를 위한 지원 기록 조회, 50자 제한, 이력서 상세한 내용은 상세조회
     */
    @Query(
            "SELECT new com.recruitment.dto.response.JobApplicationHistoryResponse(" +
                    "j.jobPosting.id,j.jobPosting.title,j.jobPosting.jobPosition,j.jobPosting.skills," +
                    "j.memberResume.id,j.memberResume.title,SUBSTRING(j.memberResume.description, 1, 50)) " +
                    "FROM JobApplicationHistory j " +
                    "WHERE j.jobPosting.id = :jobPostingId " +
                    "ORDER BY j.lastUpdate DESC"
    )
    Page<JobApplicationHistoryResponse> findAllByMemberIdOrderByLastForCompany(Pageable pageable,
                                                                               @Param("jobPostingId") Long jobPostingId);

    /**
     * 한 지원공고에 멤버가 한 번 지원가능하도록 지원 이력이 있는지 조회
     */
    boolean existsByMember_idAndJobPosting_id(Long memberId, Long jobPostingId);
}