package com.recruitment.service;

import com.recruitment.domain.JobApplicationHistory;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.Member;
import com.recruitment.domain.MemberResume;
import com.recruitment.dto.response.JobApplicationHistoryResponse;
import com.recruitment.dto.response.PagingMetaData;
import com.recruitment.dto.response.PagingResponse;
import com.recruitment.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationHistoryService {

    @Autowired
    private final JobApplicationHistoryRepository jobApplicationHistoryRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final MemberResumeRepository memberResumeRepository;
    @Autowired
    private final JobPostingRepository jobPostingRepository;

    /**
     * 지원 기록 조회, 이력서 지원
     */

    /**
     * 지원 기록 조회 - 유저의 경우 - 특정 유저가 지원한 기록 조회
     * member_id 로 조회
     * PagingResponse 사용
     * 요청 : member_id param
     * 응답 : JobApplicationHistoryResponse Dto
     */
    public PagingResponse<JobApplicationHistoryResponse> findJobApplicationHistoryForMember(Long memberId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20);
        Page<JobApplicationHistoryResponse> jobApplicationHistoryResponsePage =
                jobApplicationHistoryRepository.findAllByMemberIdOrderByLastForMember(pageable, memberId);

        List<JobApplicationHistoryResponse> content = jobApplicationHistoryResponsePage.getContent();
        int currentPage = jobApplicationHistoryResponsePage.getNumber();
        long totalElements = jobApplicationHistoryResponsePage.getTotalElements();
        int totalPages = jobApplicationHistoryResponsePage.getTotalPages();
        int itemsPerPage = jobApplicationHistoryResponsePage.getSize();

        // 필요한 메타 데이터만 전송
        PagingMetaData meta = new PagingMetaData(currentPage, totalPages, totalElements, itemsPerPage);
        return new PagingResponse<>(content, meta);
    }

    /**
     * 지원 기록 조회 - 회사의 경우 - 회사가 올린 특정 공고의 지원 이력 조회
     * job_posting_id 로 조회
     * PagingResponse 사용
     * 요청 : job_posting_id param
     * 응답 : JobApplicationHistoryResponse Dto
     */
    public PagingResponse<JobApplicationHistoryResponse> findJobApplicationHistoryForCompany(Long jobPostingId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20);
        Page<JobApplicationHistoryResponse> jobApplicationHistoryResponsePage =
                jobApplicationHistoryRepository.findAllByMemberIdOrderByLastForCompany(pageable, jobPostingId);

        List<JobApplicationHistoryResponse> content = jobApplicationHistoryResponsePage.getContent();
        int currentPage = jobApplicationHistoryResponsePage.getNumber();
        long totalElements = jobApplicationHistoryResponsePage.getTotalElements();
        int totalPages = jobApplicationHistoryResponsePage.getTotalPages();
        int itemsPerPage = jobApplicationHistoryResponsePage.getSize();

        // 필요한 메타 데이터만 전송
        PagingMetaData meta = new PagingMetaData(currentPage, totalPages, totalElements, itemsPerPage);
        return new PagingResponse<>(content, meta);
    }

    /**
     * 멤버의 이력서 채용공고 지원
     * !! 제한 사항 - 1회만 지원 가능 !!
     * member_resume_id, job_posting_id 필요
     * 요청 : memberId, member_resume_id, job_posting_id param
     * 응답 : JobApplicationHistoryResponse Dto
     */
    @Transactional
    public JobApplicationHistoryResponse addJobApplicationHistory(Long memberId,Long memberResumeId, Long jobPostingId) {
        // 이미 지원했을시 익셉션
        if(jobApplicationHistoryRepository.existsByMember_idAndJobPosting_id(memberId, jobPostingId)){
            throw new IllegalArgumentException("이미 지원하였습니다. 한 번만 지원할 수 있습니다.");
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("Invalid memberId: " + memberId)
        );

        MemberResume memberResume = memberResumeRepository.findById(memberResumeId).orElseThrow(
                () -> new IllegalArgumentException("Invalid memberResumeId: " + memberId)
        );

        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId).orElseThrow(
                () -> new IllegalArgumentException("Invalid jogPostingId: " + memberId)
        );

        JobApplicationHistory jobApplicationHistory = new JobApplicationHistory();
        jobApplicationHistory.setMember(member);
        jobApplicationHistory.setJobPosting(jobPosting);
        jobApplicationHistory.setMemberResume(memberResume);
        jobApplicationHistoryRepository.save(jobApplicationHistory);

        return JobApplicationHistoryResponse.fromEntities(jobPosting, memberResume);
    }
}
