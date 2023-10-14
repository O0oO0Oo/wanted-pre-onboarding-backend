package com.recruitment.repository;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;
import com.recruitment.dto.response.JobPostingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobPostingRepositoryTest {
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private JobPosting beforeEachJobPosting;

    @BeforeEach
    @DisplayName("Select, Delete, Update 테스트를 위한 초기 값 주입")
    void beforeEach() {
        Company company = new Company();
        company.setCompanyName("beforeEach");
        company.setCity("서울");
        company.setCountry("한국");
        company.setDescription("채용");

        // job posting
        JobPosting jobPosting = new JobPosting();
        JobPostingDetails jobPostingDetails = new JobPostingDetails();
        jobPostingDetails.setDescription("beforeEach Description");
        jobPosting.setJobPostingDetails(
                jobPostingDetails
        );
        jobPosting.setCompany(company);
        jobPosting.setCompensation(500000L);
        jobPosting.setJobPosition("신입");
        jobPosting.setTitle("신입 스프링 개발자 채용");
        jobPosting.setSkills(Arrays.asList("java", "spring"));

        // job posting 1
        JobPosting jobPosting1 = new JobPosting();
        JobPostingDetails jobPostingDetails1 = new JobPostingDetails();
        jobPostingDetails1.setDescription("beforeEach1 Description");
        jobPosting1.setJobPostingDetails(
                jobPostingDetails1
        );
        jobPosting1.setCompany(company);
        jobPosting1.setCompensation(500000L);
        jobPosting1.setJobPosition("신입1");
        jobPosting1.setTitle("신입 스프링 개발자 채용");
        jobPosting1.setSkills(Arrays.asList("java", "spring"));

        // job posting 2
        JobPosting jobPosting2 = new JobPosting();
        JobPostingDetails jobPostingDetails2 = new JobPostingDetails();
        jobPostingDetails2.setDescription("beforeEach2 Description");
        jobPosting2.setJobPostingDetails(
                jobPostingDetails2
        );
        jobPosting2.setCompany(company);
        jobPosting2.setCompensation(500000L);
        jobPosting2.setJobPosition("신입2");
        jobPosting2.setTitle("신입 스프링 개발자 채용");
        jobPosting2.setSkills(Arrays.asList("java", "spring"));

        List<JobPosting> jobPostingList = new ArrayList<>();
        jobPostingList.add(jobPosting);
        jobPostingList.add(jobPosting1);
        jobPostingList.add(jobPosting2);
        company.setJobPostings(jobPostingList);
        companyRepository.saveAndFlush(company);

        // Delete, Update
        beforeEachJobPosting = jobPostingRepository.saveAndFlush(jobPosting);

        // Select
        jobPostingRepository.saveAllAndFlush(jobPostingList);
    }

    @Test
    @DisplayName("Select - JobPosting Paging")
    void givenPageN_whenSelect_thenReturn() {
        // Given 총 3개의 데이터가 들어감
        Pageable pageable1 = PageRequest.of(0, 2);
        Pageable pageable2 = PageRequest.of(1, 2);

        // When
        Page<JobPostingResponse> page1 = jobPostingRepository.findAllByOrderByLastUpdateDesc(pageable1);
        Page<JobPostingResponse> page2 = jobPostingRepository.findAllByOrderByLastUpdateDesc(pageable2);

        // Then 총 3개의 데이터, 2개씩 페이징 1페이지 2개, 2페이지 1개
        assertThat(page1.getContent()).isNotNull();
        assertThat(page1.getContent().size()).isEqualTo(2);
        assertThat(page2.getContent()).isNotNull();
        assertThat(page2.getContent().size()).isEqualTo(1);

        // Then 0번째 보다 1번째가 늦음
        assertThat(page1.getContent().get(0).lastUpdate()).isAfter(page1.getContent().get(1).lastUpdate());  // because we ordered by lastUpdate descending

//        System.out.println(page1.getContent().get(0).getLastUpdate());
//        System.out.println(page1.getContent().get(1).getLastUpdate());
    }

    @Test
    @DisplayName("Insert - JobPosting")
    void givenTestData_whenInsert_thenSave() {
        // Given
        JobPostingDetails jobPostingDetails = new JobPostingDetails();
        jobPostingDetails.setDescription("Test Description");

        Company company = new Company();
        company.setCompanyName("원티드");
        company.setCity("서울");
        company.setCountry("한국");
        company.setDescription("채용");


        JobPosting jobPosting = new JobPosting();
        jobPosting.setJobPostingDetails(
                jobPostingDetails
        );
        jobPosting.setCompany(company);
        jobPosting.setCompensation(500000L);
        jobPosting.setJobPosition("신입");
        jobPosting.setTitle("신입 스프링 개발자 채용");
        jobPosting.setSkills(Arrays.asList("java", "spring"));

        List<JobPosting> jobPostingList = new ArrayList<>();
        jobPostingList.add(jobPosting);
        company.setJobPostings(jobPostingList);
        companyRepository.saveAndFlush(company);

        // When
        JobPosting save = jobPostingRepository.save(jobPosting);

        // Then
        assertThat(save).isEqualTo(jobPosting);
    }

    @Test
    @DisplayName("Update - JobPosting")
    void givenJobPosting_whenUpdate_thenUpdate() {
        // Given
        JobPosting jobPosting = jobPostingRepository.findById(beforeEachJobPosting.getId()).orElseThrow();
        String newTitle = "타이틀 내용 업데이트";
        jobPosting.setTitle(newTitle);

        // When
        JobPosting update = jobPostingRepository.saveAndFlush(jobPosting);

        // Then
        assertThat(newTitle).isEqualTo(update.getTitle());
    }

    @Test
    @DisplayName("Delete - JobPosting")
    void givenJobPostingId_whenDelete_thenDelete() {
        // Given

        // When
        jobPostingRepository.deleteById(beforeEachJobPosting.getId());

        // Then
        Optional<JobPosting> delete = jobPostingRepository.findById(1L);
        assertThat(delete).isEmpty();
    }

    /**
     * 동일한 회사의 채용공고 목록
     */
    @Test
    @DisplayName("findByCompanyId - JobPosting")
    void givenCompanyId_whenFind_thenFindList() {
        // Given
        // company1, job_posting 2개
        Company company1 = new Company();
        company1.setCompanyName("원티드1");
        company1.setCity("서울");
        company1.setCountry("한국");
        company1.setDescription("채용");

        JobPostingDetails jobPostingDetails1 = new JobPostingDetails();
        jobPostingDetails1.setDescription("원티드1 채용공고 1");
        JobPostingDetails jobPostingDetails2 = new JobPostingDetails();
        jobPostingDetails2.setDescription("원티드1 채용공고 2");

        JobPosting jobPosting1 = new JobPosting();
        jobPosting1.setJobPostingDetails(
                jobPostingDetails1
        );
        jobPosting1.setCompany(company1);
        jobPosting1.setCompensation(500000L);
        jobPosting1.setJobPosition("신입");
        jobPosting1.setTitle("신입 스프링 개발자 채용");
        jobPosting1.setSkills(Arrays.asList("java", "spring"));

        JobPosting jobPosting2 = new JobPosting();
        jobPosting2.setJobPostingDetails(
                jobPostingDetails2
        );
        jobPosting2.setCompany(company1);
        jobPosting2.setCompensation(500000L);
        jobPosting2.setJobPosition("신입");
        jobPosting2.setTitle("신입 파이썬 개발자 채용");
        jobPosting2.setSkills(Arrays.asList("python", "flask"));

        List<JobPosting> jobPostingList1 = new ArrayList<>();
        jobPostingList1.add(jobPosting1);
        jobPostingList1.add(jobPosting2);
        company1.setJobPostings(jobPostingList1);
        companyRepository.saveAndFlush(company1);

        // company2 , job_posting 1 개
        Company company2 = new Company();
        company2.setCompanyName("원티드2");
        company2.setCity("서울");
        company2.setCountry("한국");
        company2.setDescription("채용");

        JobPostingDetails jobPostingDetails3 = new JobPostingDetails();
        jobPostingDetails3.setDescription("원티드2 채용공고 1");

        JobPosting jobPosting3 = new JobPosting();
        jobPosting3.setJobPostingDetails(
                jobPostingDetails3
        );
        jobPosting3.setCompany(company2);
        jobPosting3.setCompensation(500000L);
        jobPosting3.setJobPosition("신입");
        jobPosting3.setTitle("신입 파이썬 개발자 채용");
        jobPosting3.setSkills(Arrays.asList("python", "flask"));

        List<JobPosting> jobPostingList2 = new ArrayList<>();
        jobPostingList2.add(jobPosting3);
        company2.setJobPostings(jobPostingList2);
        companyRepository.saveAndFlush(company2);

        // When
        List<JobPosting> jobPostingList = new ArrayList<>();
        jobPostingList.add(jobPosting1);
        jobPostingList.add(jobPosting2);
        jobPostingList.add(jobPosting3);
        jobPostingRepository.saveAll(jobPostingList);

        // Then 
        /**
         * company1 올린 공고 2개
         * company2 올린 공고 1개
         */
        assertThat(jobPostingRepository.findByCompanyId(company1.getId()).size()).isEqualTo(2);
        assertThat(jobPostingRepository.findByCompanyId(company2.getId()).size()).isEqualTo(1);
    }

    /**
     * TODO : 검색에 대한 테스트 작성할 것
     */
}