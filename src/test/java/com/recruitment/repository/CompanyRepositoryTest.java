package com.recruitment.repository;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest {
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private JobPostingDetailsRepository jobPostingDetailsRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private JobPosting beforeEachJobPosting;
    @BeforeEach
    @DisplayName("테스트를 위한 초기 값 주입")
    void beforeEach() {
        JobPostingDetails jobPostingDetails = new JobPostingDetails();
        jobPostingDetails.setDescription("beforeEach Description");

        Company company = new Company();
        company.setCompanyName("beforeEach");
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
        jobPosting.setSkills("java/spring");

        List<JobPosting> jobPostingList = new ArrayList<>();
        jobPostingList.add(jobPosting);
        company.setJobPostings(jobPostingList);
        companyRepository.saveAndFlush(company);

        beforeEachJobPosting = jobPostingRepository.saveAndFlush(jobPosting);
    }

    @Test
    @DisplayName("Delete - Company 삭제시 JobPosting, JobPostingDetails 도 cascade 삭제되는지 확인.")
    void givenJobPostingId_whenDeleteJobPosting_thenDeleteJobPostingDetails() {
        // Given
        Long companyId = beforeEachJobPosting.getCompany().getId();
        Long jobPostingDetailsId = beforeEachJobPosting.getJobPostingDetails().getId();
        Long jobPostingId = beforeEachJobPosting.getId();

        // When
        companyRepository.deleteById(companyId);
        companyRepository.flush();

        // Then 연관된것 모두 삭제
        Optional<JobPosting> deleteJobPosting = jobPostingRepository.findById(jobPostingId);
        assertThat(deleteJobPosting).isEmpty();

        Optional<JobPostingDetails> deleteJobPostingDetails = jobPostingDetailsRepository.findById(jobPostingDetailsId);
        assertThat(deleteJobPostingDetails).isEmpty();
    }
}