package com.recruitment.controller;

import com.recruitment.dto.response.JobApplicationHistoryResponse;
import com.recruitment.dto.response.PagingResponse;
import com.recruitment.service.JobApplicationHistoryService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.results.implicit.ImplicitModelPartResultBuilderEmbeddable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class JobApplicationHistoryController {

    @Autowired
    private final JobApplicationHistoryService jobApplicationHistoryService;

    /**
     * 지원 기록 조회, 이력서 지원
     */
    @GetMapping("/member")
    public ResponseEntity<PagingResponse<JobApplicationHistoryResponse>> listJobApplicationHistoryForMember(
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "page") int page
    ) {
        return ResponseEntity.ok(jobApplicationHistoryService.findJobApplicationHistoryForMember(
                memberId, page
        ));
    }

    @GetMapping("/company")
    public ResponseEntity<PagingResponse<JobApplicationHistoryResponse>> listJobApplicationHistoryForCompany(
            @RequestParam(name = "jobPostingId") Long jobPostingId,
            @RequestParam(name = "page") int page
    ) {
        return ResponseEntity.ok(jobApplicationHistoryService.findJobApplicationHistoryForCompany(
                jobPostingId, page
        ));
    }

    @PostMapping
    public ResponseEntity<JobApplicationHistoryResponse> addJobApplicationHistory(
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "memberResumeId") Long memberResumeId,
            @RequestParam(name = "jobPostingId") Long jobPostingId
    ) {
        return new ResponseEntity<>(
                jobApplicationHistoryService.addJobApplicationHistory(
                        memberId, memberResumeId, jobPostingId
                ), HttpStatus.CREATED
        );
    }
}
