package com.recruitment.repository;

import com.recruitment.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberResumeRepository memberResumeRepository;
    @Autowired
    private JobApplicationHistoryRepository jobApplicationHistoryRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private EntityManager entityManager;

    private Member member;
    private List<MemberResume> memberResumeList;
    private JobApplicationHistory jobApplicationHistory;
    @BeforeEach
    @DisplayName("테스트 초기 값 주입")
    void beforeEach() {
        member = new Member();

        member.setEmail("wanted@gmail.com");
        member.setName("wanted");
        member.setPhone("010010100");

        memberRepository.save(member);

        MemberResume memberResume1 = new MemberResume();
        MemberResume memberResume2 = new MemberResume();
        MemberResume memberResume3 = new MemberResume();

        memberResume1.setMember(member);
        memberResume2.setMember(member);
        memberResume3.setMember(member);

        memberResume1.setTitle("mem1");
        memberResume2.setTitle("mem2");
        memberResume3.setTitle("mem3");

        memberResume1.setDescription("mem1");
        memberResume2.setDescription("mem2");
        memberResume3.setDescription("mem3");

        memberResumeList = new ArrayList<>();
        memberResumeList.add(memberResume1);
        memberResumeList.add(memberResume2);
        memberResumeList.add(memberResume3);

        // 지원이력 저장, 연관 엔티티 생성 및 저장
        Company company = new Company();
        company.setCompanyName("asd");
        company.setCountry("asd");
        company.setDescription("ads");
        company.setCity("asd");

        JobPosting jobPosting = new JobPosting();
        jobPosting.setCompany(company);
        jobPosting.setTitle("asd");
        jobPosting.setSkills("asd");
        jobPosting.setCompensation(10L);
        jobPosting.setJobPosition("asd");
        JobPostingDetails jobPostingDetails = new JobPostingDetails();
        jobPostingDetails.setDescription("asd");
        jobPosting.setJobPostingDetails(jobPostingDetails);

        companyRepository.save(company);
        jobPostingRepository.save(jobPosting);

        jobApplicationHistory = new JobApplicationHistory();
        jobApplicationHistory.setMemberResume(memberResume1);
        jobApplicationHistory.setJobPosting(jobPosting);

        memberResumeRepository.saveAll(memberResumeList);
        jobApplicationHistoryRepository.saveAndFlush(jobApplicationHistory);
    }

    @Test
    @DisplayName("Member 삭제 시 MemberResume, JobApplicationHistory Cascade 삭제")
    public void given_when_then(){
        // Given
        entityManager.clear();

        // When
        memberRepository.deleteById(member.getId());

        // Then
        assertThat(memberResumeRepository.findById(memberResumeList.get(0).getId()).isPresent())
                .isFalse();
        assertThat(memberResumeRepository.findById(memberResumeList.get(1).getId()).isPresent())
                .isFalse();
        assertThat(memberResumeRepository.findById(memberResumeList.get(2).getId()).isPresent())
                .isFalse();

        assertThat(memberRepository.findById(member.getId()).isPresent())
                .isFalse();

        assertThat(jobApplicationHistoryRepository.findById(jobApplicationHistory.getId()).isPresent())
                .isFalse();
    }
}