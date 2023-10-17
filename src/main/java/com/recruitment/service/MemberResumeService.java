package com.recruitment.service;

import com.recruitment.domain.Member;
import com.recruitment.domain.MemberResume;
import com.recruitment.dto.request.MemberResumeAddRequest;
import com.recruitment.dto.request.MemberResumeModifyRequest;
import com.recruitment.dto.response.MemberResumeResponse;
import com.recruitment.dto.response.PagingMetaData;
import com.recruitment.dto.response.PagingResponse;
import com.recruitment.repository.MemberRepository;
import com.recruitment.repository.MemberResumeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberResumeService {

    private final MemberRepository memberRepository;
    private final MemberResumeRepository memberResumeRepository;

    /**
     * 이력서 리스트조회, 등록, 수정, 삭제, 상세조회
     */

    /**
     * 이력서 조회
     * member_id 로 조회
     * PagingResponse 로 이력서 목록 조회
     * title, description 은 50자 제한 출력
     * 요청 : member_id param
     * 응답 : MemberResumeResponse Dto
     */
    public PagingResponse<MemberResumeResponse> findMemberResume(Long memberId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20);
        Page<MemberResumeResponse> memberResumeResponsePage = memberResumeRepository.findAllByMemberIdOrderByLast(pageable, memberId);

        List<MemberResumeResponse> content = memberResumeResponsePage.getContent();
        int currentPage = memberResumeResponsePage.getNumber();
        long totalElements = memberResumeResponsePage.getTotalElements();
        int totalPages = memberResumeResponsePage.getTotalPages();
        int itemsPerPage = memberResumeResponsePage.getSize();

        // 필요한 메타 데이터만 전송
        PagingMetaData meta = new PagingMetaData(currentPage, totalPages, totalElements, itemsPerPage);
        PagingResponse<MemberResumeResponse> response = new PagingResponse<>(content, meta);

        return response;
    }

    /**
     * 이력서 등록
     * Member_id 필요
     * title, description 작성
     * 요청 : ResumeAddRequest Dto
     * 응답 : MemberResumeResponse Dto
     */
    @Transactional
    public MemberResumeResponse addMemberResume(MemberResumeAddRequest memberResumeAddRequest) {
        // 멤버 찾기, 없는 멤버 ID 에러 반환
        Member member = memberRepository.findById(memberResumeAddRequest.memberId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid memberId: " + memberResumeAddRequest.memberId())
                );

        MemberResume memberResume = new MemberResume();
        memberResume.setMember(member);
        memberResume.setTitle(memberResumeAddRequest.title());
        memberResume.setDescription(memberResumeAddRequest.description());

        MemberResume savedMemberResume = memberResumeRepository.save(memberResume);

        return MemberResumeResponse.fromEntity(savedMemberResume);
    }

    /**
     *  이력서 삭제
     *  member_resume_id 필요
     *  요청 : member_resume_id param
     *  응답 : "삭제되었습니다." String
     */
    public String deleteMemberResume(Long memberResumeId) {
        MemberResume memberResume = memberResumeRepository.findById(memberResumeId).orElseThrow(
                () -> new IllegalArgumentException("Invalid memberResumeId: " + memberResumeId)
        );

        memberResumeRepository.delete(memberResume);

        return "삭제되었습니다.";
    }

    /**
     * 이력서 수정
     * Member_id, member_resume_id 필요
     * title, description 작성
     * 요청 : ResumeModifyRequest Dto
     * 응답 : MemberResumeResponse Dto
     */
    @Transactional
    public MemberResumeResponse modifyMemberResume(MemberResumeModifyRequest memberResumeModifyRequest) {
        MemberResume memberResume = memberResumeRepository.findByIdAndMemberId(
                memberResumeModifyRequest.memberResumeId(),
                memberResumeModifyRequest.memberId()
        ).orElseThrow(
                () -> new IllegalArgumentException("Invalid memberId, memberResumeId: " + memberResumeModifyRequest.memberId() + " or " + memberResumeModifyRequest.memberResumeId())
        );

        memberResume.setTitle(memberResumeModifyRequest.title());
        memberResume.setDescription(memberResumeModifyRequest.description());

        MemberResume savedMemberResume = memberResumeRepository.save(memberResume);

        return MemberResumeResponse.fromEntity(savedMemberResume);
    }

    /**
     * 이력서 상세 조회
     * member_resume_id 필요
     * title, description 모두 출력
     * 요청 : member_id, member_resume_id param
     * 응답 : MemberResumeResponse Dto
     */
    public MemberResumeResponse findMemberResumeDetails(Long memberResumeId) {
        MemberResume memberResume = memberResumeRepository.findById(memberResumeId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid memberResumeId: " + memberResumeId)
                );
        return MemberResumeResponse.fromEntity(memberResume);
    }
}
