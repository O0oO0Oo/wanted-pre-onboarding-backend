package com.recruitment.service;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;
import com.recruitment.dto.mapping.MappingJobPostingResponse;
import com.recruitment.dto.request.JobPostingAddRequest;
import com.recruitment.dto.request.JobPostingModifyRequest;
import com.recruitment.dto.response.*;
import com.recruitment.repository.CompanyRepository;
import com.recruitment.repository.JobPostingDetailsRepository;
import com.recruitment.repository.JobPostingRepository;
import com.recruitment.utils.NLPServer;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingDetailsRepository jobPostingDetailsRepository;
    private final CompanyRepository companyRepository;
    private final EntityManager entityManager;
    private final NLPServer nlpServer;
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

    /**
     * 등록 키워드 분석 후 저장
     * @param jobPostingAddRequest
     * @return
     */
    @Transactional
    public JobPostingResponse addJobPosting(JobPostingAddRequest jobPostingAddRequest) {
        // 채용 공고 상세내용 등록
        JobPostingDetails jobPostingDetails = new JobPostingDetails();
        jobPostingDetails.setDescription(jobPostingAddRequest.jobPostingDetails());
        JobPostingDetails savedJobPostingDetails = jobPostingDetailsRepository.save(jobPostingDetails);

        // 이전 코드의 프록시로 불러오면 안됨. 없는 회사의 ID 라면 에러 반환
        Company company = companyRepository.findById(jobPostingAddRequest.companyId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid companyId: " + jobPostingAddRequest.companyId())
                );

        // 채용 공고 등록

        /**
         * 검색 내용에 들어갈 내용
         * 회사 이름, 국가, 위치
         * 채용공고 제목, 상세내용, 포지션, 스킬
         */
        String keyword =
                company.getCompanyName() + " " +
                company.getCountry() + " " +
                company.getCity() + " " +
                jobPostingAddRequest.title() + " " +
                jobPostingAddRequest.jobPostingDetails() + " " +
                jobPostingAddRequest.jobPosition() + " " +
                jobPostingAddRequest.skills();
        String nlp = nlpServer.morphsKeyword(keyword);
        System.out.println("nlp = " + nlp);

        jobPostingRepository.saveJobPosting(
                company.getId(),
                savedJobPostingDetails.getId(),
                jobPostingAddRequest.title(),
                jobPostingAddRequest.jobPosition(),
                jobPostingAddRequest.compensation(),
                jobPostingAddRequest.skills(),
                nlp,
                ZonedDateTime.now()
        );

        // JobPostingDetails 과는 1대1 매칭이므로 저장된것 확인하기
        JobPosting savedJobPosting = jobPostingRepository.findByJobPostingDetailsId(savedJobPostingDetails.getId());

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

        Company company = jobPosting.getCompany();

        // 수정시 다시 nlp 수행
        String keyword =
                company.getCompanyName() + " " +
                        company.getCountry() + " " +
                        company.getCity() + " " +
                        jobPostingModifyRequest.title() + " " +
                        jobPostingModifyRequest.jobPostingDetails() + " " +
                        jobPostingModifyRequest.jobPosition() + " " +
                        jobPostingModifyRequest.skills();
        String nlp = nlpServer.morphsKeyword(keyword);

        jobPostingRepository.updateJobPosting(
                jobPosting.getId(),
                jobPostingModifyRequest.title(),
                jobPostingModifyRequest.jobPosition(),
                jobPostingModifyRequest.compensation(),
                jobPostingModifyRequest.skills(),
                nlp,
                ZonedDateTime.now()
        );

        entityManager.flush();
        // 저장 결과 불러오기
        JobPosting savedJobPosting = jobPostingRepository.findById(jobPosting.getId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid jobPostingId: " + jobPostingModifyRequest.jobPostingId())
        );

        return JobPostingModifyResponse.fromEntities(savedJobPosting, jobPostingDetails);
    }

    // 삭제
    public String deleteJobPosting(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid jobPostingId: " + jobPostingId));

        jobPostingRepository.delete(jobPosting);

        return "삭제되었습니다.";
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
    public PagingResponse<JobPostingResponse> findSearchText(String keyword, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20);

        // nlp server 로 형태소 분석
        String nlp = nlpServer.morphsKeyword(keyword);
        System.out.println("nlp = " + nlp);
        // 형태소 분석한 키워드로 검색
        // WHERE search_text_tsvector @@ to_tsquery('검색어1&검색어2&...&검색어n') 을 매칭
        Page<MappingJobPostingResponse> jobPostingResponsePage =
                jobPostingRepository.searchText(pageable, nlp.replace(" ","&"));


        // 리스트 매핑
        List<JobPostingResponse> content = jobPostingResponsePage.getContent().stream()
                .map(
                        mappingJobPostingResponse -> {
                            return JobPostingResponse.fromMappingDto(mappingJobPostingResponse);
                        }
                ).collect(Collectors.toList());
        int currentPage = jobPostingResponsePage.getNumber();
        long totalElements = jobPostingResponsePage.getTotalElements();
        int totalPages = jobPostingResponsePage.getTotalPages();
        int itemsPerPage = jobPostingResponsePage.getSize();

        // 필요한 메타 데이터만 전송
        PagingMetaData meta = new PagingMetaData(currentPage, totalPages, totalElements, itemsPerPage);
        PagingResponse<JobPostingResponse> response = new PagingResponse<>(content, meta);

        return response;
    }
}
