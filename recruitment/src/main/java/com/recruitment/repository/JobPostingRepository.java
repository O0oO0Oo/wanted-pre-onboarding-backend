package com.recruitment.repository;

import com.recruitment.domain.JobPosting;
import com.recruitment.dto.mapping.MappingJobPostingResponse;
import com.recruitment.dto.response.JobPostingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    /**
     * Company 와 Join 후에 최신 업데이트 순서로 paging
     */
    @Query("SELECT new com.recruitment.dto.response.JobPostingResponse(j.id, c.companyName, c.id ,c.country, c.city, j.jobPosition, j.compensation, j.skills, j.lastUpdate) " +
            "FROM JobPosting j JOIN j.company c ORDER BY j.lastUpdate DESC")
    Page<JobPostingResponse> findAllByOrderByLastUpdateDesc(Pageable pageable);

    /**
     * TODO : 걷색 기능, 자연어 처리 라이브러리 연동 하면서 구현
     */

    /**
     * 회사가 올린 다른 채용 공고 id list
     */
    List<JobPosting> findByCompanyId(Long companyId);

    /**
     * 검색 쿼리
     */
    @Query(value =
            "SELECT " +
                    "  j.job_posting_id as jobPostingId, " +
                    "  c.company_name as companyName, " +
                    "  c.company_id as companyId, " +
                    "  c.country as country, " +
                    "  c.city as city, " +
                    "  j.job_position as jobPosition, " +
                    "  j.compensation as compensation, " +
                    "  j.skills as skills, " +
                    "  j.last_update as lastUdate " +
                    "FROM job_posting j " +
                    "JOIN company c ON j.company_id = c.company_id " +
                    "WHERE j.search_text_tsvector @@ to_tsquery(:keyword) " +
                    "ORDER BY j.last_update DESC",
            nativeQuery = true)
    Page<MappingJobPostingResponse> searchText(Pageable pageable, @Param("keyword") String keyword);

    @Modifying
    @Query(value = "" +
            "INSERT INTO job_posting" +
            "(company_id, job_posting_details_id, title, job_position, compensation, skills, search_text_tsvector, last_update) " +
            "VALUES(:company_id,:job_posting_details_id,:title,:job_position,:compensation,:skills, to_tsvector(:search_text_tsvector), :last_update)",
            nativeQuery = true
    )
    void saveJobPosting(
            @Param("company_id") Long company_id,
            @Param("job_posting_details_id") Long job_posting_details_id,
            @Param("title") String title,
            @Param("job_position") String job_position,
            @Param("compensation") Long compensation,
            @Param("skills") String skills,
            @Param("search_text_tsvector") String search_text_tsvector,
            @Param("last_update")ZonedDateTime lastUpdate
            );

    /**
     * tsvector 추가로 인한 에러로 인해 수정
     */
    @Modifying
    @Query(value = "" +
            "UPDATE job_posting " +
            "SET " +
            "title = :title, " +
            "job_position = :job_position, " +
            "compensation = :compensation, " +
            "skills = :skills, " +
            "search_text_tsvector = to_tsvector(:search_text_tsvector)," +
            "last_update = :last_update " +
            "WHERE job_posting_id = :job_posting_id",
            nativeQuery = true)
    void updateJobPosting(
            @Param("job_posting_id") Long jobPostingId,
            @Param("title") String title,
            @Param("job_position") String job_position,
            @Param("compensation") Long compensation,
            @Param("skills") String skills,
            @Param("search_text_tsvector") String search_text_tsvector,
            @Param("last_update")ZonedDateTime lastUpdate
    );

    JobPosting findByJobPostingDetailsId(Long jobPostingDetailsId);
}




