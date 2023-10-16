package com.recruitment.service;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;
import com.recruitment.dto.request.JobPostingAddRequest;
import com.recruitment.dto.request.JobPostingModifyRequest;
import com.recruitment.dto.response.*;
import com.recruitment.repository.CompanyRepository;
import com.recruitment.repository.JobPostingDetailsRepository;
import com.recruitment.repository.JobPostingRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingDetailsRepository jobPostingDetailsRepository;
    private final CompanyRepository companyRepository;
    private final EntityManager entityManager;

    /**
     * 목록, 등록, 수정, 삭제, 상세페이지, 키워드 검색기능
     */
    // 목록
    public PagingResponse<JobPostingResponse> findJobPosting(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20);
        Page<JobPostingResponse> jobPostingResponsePage = jobPostingRepository.findAllByOrderByLastUpdateDesc(pageable);

        List<JobPostingResponse> content = jobPostingResponsePage.getContent();
        int currentPage = jobPostingResponsePage.getNumber();
        long totalElements = jobPostingResponsePage.getTotalElements();
        int totalPages = jobPostingResponsePage.getTotalPages();
        int itemsPerPage = jobPostingResponsePage.getSize();

        // 필요한 메타 데이터만 전송
        PagingMetaData meta = new PagingMetaData(currentPage, totalPages, totalElements, itemsPerPage);
        PagingResponse<JobPostingResponse> response = new PagingResponse<>(content, meta);

        return response;
    }
    
    // 등록
    @Transactional
    public JobPostingResponse addJobPosting(JobPostingAddRequest jobPostingAddRequest) {
        // 채용 공고 상세내용 등록
        JobPostingDetails jobPostingDetails = new JobPostingDetails();
        jobPostingDetails.setDescription(jobPostingAddRequest.jobPostingDetails());
        JobPostingDetails saveedJobPostingDetails = jobPostingDetailsRepository.save(jobPostingDetails);

        // 이전 코드의 프록시로 불러오면 안됨. 없는 회사의 ID 라면 에러 반환
        Company company = companyRepository.findById(jobPostingAddRequest.companyId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid companyId: " + jobPostingAddRequest.companyId())
                );

        // 채용 공고 등록
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle(jobPostingAddRequest.title());
        jobPosting.setJobPosition(jobPostingAddRequest.jobPosition());
        jobPosting.setCompensation(jobPostingAddRequest.compensation());
        jobPosting.setJobPostingDetails(saveedJobPostingDetails);
        jobPosting.setCompany(company);
        jobPosting.setSkills(jobPostingAddRequest.skills());

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

        return JobPostingResponse.fromEntities(savedJobPosting, company);
    }

    // 수정 TODO: Exception
    @Transactional
    public JobPostingModifyResponse modifyJobPosting(JobPostingModifyRequest jobPostingModifyRequest) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingModifyRequest.jobPostingId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid jobPostingId: " + jobPostingModifyRequest.jobPostingId())
                );
        JobPostingDetails jobPostingDetails = jobPosting.getJobPostingDetails();
        jobPostingDetails.setDescription(jobPostingModifyRequest.jobPostingDetails());

        jobPosting.setTitle(jobPostingModifyRequest.title());
        jobPosting.setJobPosition(jobPostingModifyRequest.jobPosition());
        jobPosting.setCompensation(jobPostingModifyRequest.compensation());
        jobPosting.setSkills(jobPostingModifyRequest.skills());

        return JobPostingModifyResponse.fromEntity(jobPostingRepository.save(jobPosting), jobPostingDetails);
    }

    // 삭제
    public String deleteJobPosting(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid jobPostingId: " + jobPostingId));

        jobPostingRepository.delete(jobPosting);

        return "삭제 되었습니다.";
    }

    // 상세 페이지, 동일 회사 공고 id list
    public JobPostingDetailResponse findJobPostingDetails(Long jobPostingId) {
        // id로 채용공고 찾기
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId).orElseThrow(
                () -> new IllegalArgumentException("Invalid jobPostingId: " + jobPostingId)
        );

        Company company = jobPosting.getCompany();

        // 찾는 채용공고와 동일한 회사가 올린 채용공고 목록을 찾음
        List<Long> sameCompanyJobPostingIds = jobPostingRepository.findByCompanyId(company.getId()).stream().map(
                j -> j.getId()
        ).collect(Collectors.toList());

        return JobPostingDetailResponse.fromEntities(jobPosting,jobPosting.getJobPostingDetails(), company, sameCompanyJobPostingIds);
    }

    // TODO: 키워드 검색
    public void findSearchText() {

    }
}
