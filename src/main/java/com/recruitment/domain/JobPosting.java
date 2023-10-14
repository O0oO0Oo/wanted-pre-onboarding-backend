package com.recruitment.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
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
     * 구분자 '/' 를 사용해서 python/java 이런 방식으로 저장
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String skills;

    /**
     * LIKE '%text%' 검색을 위한 열, trgm index 설정
     */
    private String searchTextTrgm;
    @UpdateTimestamp
    private ZonedDateTime lastUpdate;


    /**
     * skill 을 위한 set, get 정의
     * delimiter = "/"
     */
    public void setSkills(List<String> skillList) {
        this.skills = String.join("/",skillList);
    }
    public List<String> getSkills() {
        return Arrays.stream(this.skills.split("/")).toList();
    }

    /**
     * TODO: 자연어 처리 라이브러리를 사용하여 저장
     */
    public void setSearchTextTrgm() {
        this.searchTextTrgm = "";
    }
}
