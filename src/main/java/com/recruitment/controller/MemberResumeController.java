package com.recruitment.controller;

import com.recruitment.dto.request.MemberResumeAddRequest;
import com.recruitment.dto.request.MemberResumeModifyRequest;
import com.recruitment.dto.response.MemberResumeResponse;
import com.recruitment.dto.response.PagingResponse;
import com.recruitment.service.MemberResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class MemberResumeController {

    @Autowired
    private final MemberResumeService memberResumeService;

    /**
     * 이력서 리스트조회, 등록, 수정, 삭제, 상세조회
     */

    // 이력서 조회
    @GetMapping
    public ResponseEntity<PagingResponse<MemberResumeResponse>> listMemberResume(
            @RequestParam(name = "memberId") Long memberId, @RequestParam(name = "page") int pageNumber
    ) {
        return ResponseEntity.ok(memberResumeService.findMemberResume(memberId, pageNumber));
    }

    // 이력서 등록
    @PostMapping
    public ResponseEntity<MemberResumeResponse> addMemberResume(
            @RequestBody @Valid MemberResumeAddRequest request
    ) {
        return new ResponseEntity<>(memberResumeService.addMemberResume(request), HttpStatus.CREATED);
    }

    // 이력서 수정
    @PutMapping
    public ResponseEntity<MemberResumeResponse> modifyMemberResume(
            @RequestBody @Valid MemberResumeModifyRequest request
    ) {
        return ResponseEntity.ok(memberResumeService.modifyMemberResume(request));
    }

    // 이력서 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteMemberResume(
            @RequestParam(name = "memberResumeId") Long id
    ) {
        return ResponseEntity.ok(memberResumeService.deleteMemberResume(id));
    }

    // 이력서 상세 조회
    @GetMapping("/details")
    public ResponseEntity<MemberResumeResponse> findMemberResumeDetails(
            @RequestParam(name = "memberResumeId") Long id
    ) {
        return ResponseEntity.ok(memberResumeService.findMemberResumeDetails(id));
    }
}
