package com.recruitment.repository;

import com.recruitment.domain.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    /**
     * 최신 업데이트 순서로 pagable
     */
    Page<JobPosting> findAllByOrderByLastUpdateDesc(Pageable pageable);

    /**
     * TODO : 걷색 기능, 자연어 처리 라이브러리 연동 하면서 구현
     */

    /**
     * 회사가 올린 다른 채용 공고 id list
     */
    List<JobPosting> findByCompanyId(Long companyId);
}
