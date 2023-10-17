package com.recruitment.repository;

import com.recruitment.domain.*;
import com.recruitment.dto.response.MemberResumeResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberResumeRepositoryTest {

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
    @DisplayName("findAllByMemberIdOrderByLast - MemberResumePaging 테스트")
    public void givenValue_whenSelect_thenReturn() {
        // Given
        Pageable pageable1 = PageRequest.of(0, 2);
        Pageable pageable2 = PageRequest.of(1, 2);

        // When
        Page<MemberResumeResponse> page1 = memberResumeRepository.findAllByMemberIdOrderByLast(pageable1, member.getId());
        Page<MemberResumeResponse> page2 = memberResumeRepository.findAllByMemberIdOrderByLast(pageable2, member.getId());

        // Then 총 3개를 넣었고 2개 단위로 페이지 요청
        assertThat(page1.getContent().size()).isEqualTo(2);
        assertThat(page2.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("MemberResume 삭제 시 Member 은 남아있고, JobApplicationHistory 는 삭제 되는지 확인")
    public void givenData_whenDeleteMemberResume_thenMemberPresentAndJobApplicationHistoryDeleted(){
        // Given 시작하기 컨텍스트 클리어
        entityManager.clear();
        MemberResume memberResume = memberResumeList.get(0);

        // When
        memberResumeRepository.deleteById(memberResume.getId());

        // Then MemberResume 삭제 시 Member 그대로, JobApplicationHistory 은 Cascade 삭제
        assertThat(memberResumeRepository.findById(memberResume.getId()).isPresent())
                .isFalse();
        assertThat(memberRepository.findById(member.getId()).isPresent())
                .isTrue();
        assertThat(jobApplicationHistoryRepository.findById(jobApplicationHistory.getId()).isPresent())
                .isFalse();
    }
}