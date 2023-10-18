package com.recruitment.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_posting_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_posting_details_id", nullable = false)
    private JobPostingDetails jobPostingDetails;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String jobPosition;
    @Column(nullable = false)
    private Long compensation;

    /**
     * 지원 이력, 공고가 삭제되면 지원이력도 삭제
     */
    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplicationHistory> jobApplicationHistories = new ArrayList<>();

    /**
     * 구분자 '/' 를 사용해서 python/java 이런 방식으로 저장
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String skills;

    /**
     * LIKE '%text%' 검색을 위한 열, trgm index 설정 X
     * trgm -> to_tsvector 변경
     * tsvector 타입으로 생성
     * search_text_tsvector tsvector
     * 인덱스 생성
     * CREATE INDEX idx_search_text_tsvector ON job_posting USING gin(search_text_tsvector);
     * 걷색
     * select * from job_posting where search_text_tsvector @@ to_tsquery('경험자 &백엔드 & spring&msa')
     *
     * TODO : DB에는 TSVECTOR 타입이고 Domain 에서는 String 타입이라 계속 에러가 발생 - 수정
     * - JPQL 로 넣을시 다음과 같은 에러
     * SQL Error: 0, SQLState: 42804
     * ERROR: column "search_text_tsvector" is of type tsvector but expression is of type character varying
     *   Hint: You will need to rewrite or cast the expression.
     *
     * - nativeQuery = True 로 한다음 to_tsvector() 변환 후 입력시에는 다음과 같은 에러
     * SQL Error: 0, SQLState: 02000
     * No results were returned by
     *
     * @Modify 사용 드디어 해결
     */
    @Column(columnDefinition = "TSVECTOR")
    private String searchTextTsvector;

    @UpdateTimestamp
    private ZonedDateTime lastUpdate;
}
