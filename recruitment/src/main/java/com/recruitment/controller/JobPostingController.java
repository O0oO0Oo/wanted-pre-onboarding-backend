package com.recruitment.controller;

import com.recruitment.dto.request.JobPostingAddRequest;
import com.recruitment.dto.request.JobPostingModifyRequest;
import com.recruitment.dto.response.JobPostingDetailResponse;
import com.recruitment.dto.response.JobPostingModifyResponse;
import com.recruitment.dto.response.JobPostingResponse;
import com.recruitment.dto.response.PagingResponse;
import com.recruitment.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    // 목록 조회
    @GetMapping
    public ResponseEntity<PagingResponse<JobPostingResponse>> listJobPostings(@RequestParam(name = "page")
                                                                              int pageNumber) {
        return ResponseEntity.ok(jobPostingService.findJobPosting(pageNumber));
    }

    // 채용 공고 등록
    @PostMapping
    public ResponseEntity<JobPostingResponse> addJobPosting(@RequestBody @Valid
                                                            JobPostingAddRequest request) {
        return new ResponseEntity<>(jobPostingService.addJobPosting(request), HttpStatus.CREATED);
    }

    // 채용 공고 수정
    @PutMapping
    public ResponseEntity<JobPostingModifyResponse> modifyJobPosting(@RequestBody @Valid
                                                                     JobPostingModifyRequest request) {
        return ResponseEntity.ok(jobPostingService.modifyJobPosting(request));
    }

    // 채용 공고 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteJobPosting(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(jobPostingService.deleteJobPosting(id));
    }

    // 채용 공고 상세 조회
    @GetMapping("/details")
    public ResponseEntity<JobPostingDetailResponse> findJobPostingDetails(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(jobPostingService.findJobPostingDetails(id));
    }

    // 채용 공고 키워드 검색
    @GetMapping("/search")
    public ResponseEntity<PagingResponse<JobPostingResponse>> searchListJobPostings(
            @RequestParam("keyword") String keyword,
            @RequestParam("page") int pageNumber
    ) {
        return ResponseEntity.ok(jobPostingService.findSearchText(keyword, pageNumber));
    }
}
