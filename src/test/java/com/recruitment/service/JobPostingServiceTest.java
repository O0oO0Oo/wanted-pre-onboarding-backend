package com.recruitment.service;

import com.recruitment.domain.Company;
import com.recruitment.domain.JobPosting;
import com.recruitment.domain.JobPostingDetails;
import com.recruitment.dto.request.JobPostingAddRequest;
import com.recruitment.dto.request.JobPostingModifyRequest;
import com.recruitment.dto.response.JobPostingDetailResponse;
import com.recruitment.dto.response.JobPostingModifyResponse;
import com.recruitment.dto.response.JobPostingResponse;
import com.recruitment.dto.response.PagingResponse;
import com.recruitment.repository.JobPostingDetailsRepository;
import com.recruitment.repository.JobPostingRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class JobPostingServiceTest {

    @Mock
    private JobPostingRepository jobPostingRepository;
    @Mock
    private JobPostingDetailsRepository jobPostingDetailsRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JobPostingService jobPostingService;

    // 기본 조회
    @Test
    @DisplayName("findJobPosting - 성공")
    public void givenPageNumber_whenFindList_thenSucceed() {
        // Given
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, 20);
        Page<JobPostingResponse> mockJobPostingResponsePage = mock(Page.class);
        List<JobPostingResponse> mockContent = List.of(mock(JobPostingResponse.class));

        when(jobPostingRepository.findAllByOrderByLastUpdateDesc(pageable))
                .thenReturn(mockJobPostingResponsePage);
        when(mockJobPostingResponsePage.getContent()).thenReturn(mockContent);
        when(mockJobPostingResponsePage.getNumber()).thenReturn(pageNumber);
        when(mockJobPostingResponsePage.getTotalElements()).thenReturn(1000L);
        when(mockJobPostingResponsePage.getTotalPages()).thenReturn(5);
        when(mockJobPostingResponsePage.getSize()).thenReturn(20);

        // When
        PagingResponse<JobPostingResponse> response = jobPostingService.findJobPosting(pageNumber);

        // Then
        assertThat(response).isNotNull();
        assertThat(mockJobPostingResponsePage.getSize()).isEqualTo(response.metaData().itemsPerPage());
    }

    // 등록 성공
    @Test
    @DisplayName("addJobPosting - 성공")
    public void givenJobPostingRequest_whenAdd_thenSucceed(){
        // Given
        JobPostingAddRequest jobPostingAddRequest = mock(JobPostingAddRequest.class);
        JobPostingDetails jobPostingDetails = mock(JobPostingDetails.class);
        Company company = mock(Company.class);
        JobPosting jobPosting = mock(JobPosting.class);

        when(jobPostingDetailsRepository.save(Mockito.any(JobPostingDetails.class)))
                .thenReturn(jobPostingDetails);
        when(jobPostingRepository.save(Mockito.any(JobPosting.class)))
                .thenReturn(jobPosting);
        when(entityManager.getReference(Mockito.eq(Company.class), Mockito.anyLong()))
                .thenReturn(company);

        // When
        JobPostingResponse response = jobPostingService.addJobPosting(jobPostingAddRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(jobPosting.getId()).isEqualTo(response.jobPostingId());
        assertThat(company.getId()).isEqualTo(response.companyId());

        // Verify
        verify(jobPostingDetailsRepository).save(Mockito.any(JobPostingDetails.class));
        verify(jobPostingRepository).save(Mockito.any(JobPosting.class));
        verify(entityManager).getReference(Mockito.eq(Company.class),Mockito.anyLong());
    }

    // 등록 실패
    @Test
    @DisplayName("addJobPosting - 실패 롤백")
    public void givenJobPostingRequest_whenAdd_thenRollback(){
        // Given
        JobPostingAddRequest jobPostingAddRequest = new JobPostingAddRequest(
                1L,
                "test details",
                "title",
                "jobposition",
                100L,
                "python"
        );

        // When
        // 저장 시 실패
        Mockito.doThrow(new RuntimeException("저장 실패"))
                .when(jobPostingDetailsRepository)
                .save(Mockito.any(JobPostingDetails.class));

        // 메세지 확인
        try {
            jobPostingService.addJobPosting(jobPostingAddRequest);
        } catch (RuntimeException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("저장 실패");
        }

        // Then
        // 저장, jobPostingRepository 호출 없음
        Mockito.verify(jobPostingRepository, Mockito.never())
                .save(Mockito.any(JobPosting.class));
        Assertions.assertThat(jobPostingRepository.findAll()).isEmpty();
    }

    // 수정
    @Test
    @DisplayName("modifyJobPosting - 수정")
    public void givenJobPostingModifyRequest_whenModify_thenSucceed(){
        // Given
        Long mockJobPostingId = 1L;
        String mockTitle = "new title";
        String mockJobPosition = "new position";
        Long mockCompensation = 10L;
        String mockSkills = "Java/Spring";
        String mockDescription = "new description";

        JobPostingModifyRequest jobPostingModifyRequest = mock(JobPostingModifyRequest.class);
        JobPosting jobPosting = mock(JobPosting.class);
        JobPostingDetails jobPostingDetails = mock(JobPostingDetails.class);
        JobPosting updatedJobPosting = mock(JobPosting.class);

        when(jobPostingModifyRequest.jobPostingId()).thenReturn(mockJobPostingId);
        when(jobPostingRepository.findById(mockJobPostingId))
                .thenReturn(Optional.of(jobPosting));
        when(jobPosting.getJobPostingDetails())
                .thenReturn(jobPostingDetails);

        // 새로운 값으로 변경
        when(jobPostingModifyRequest.title())
                .thenReturn(mockTitle);
        when(jobPostingModifyRequest.jobPosition())
                .thenReturn(mockJobPosition);
        when(jobPostingModifyRequest.compensation())
                .thenReturn(mockCompensation);
        when(jobPostingModifyRequest.skills())
                .thenReturn(mockSkills);
        when(jobPostingModifyRequest.jobPostingDetails())
                .thenReturn(mockDescription);
        when(jobPostingRepository.save(Mockito.any(JobPosting.class)))
                .thenReturn(updatedJobPosting);

        // When
        JobPostingModifyResponse response = jobPostingService.modifyJobPosting(jobPostingModifyRequest);

        // Then
        assertThat(response).isNotNull();

        // Verify
        verify(jobPostingDetails).setDescription(mockDescription);
        verify(jobPosting).setTitle(mockTitle);
        verify(jobPosting).setJobPosition(mockJobPosition);
        verify(jobPosting).setCompensation(mockCompensation);
        verify(jobPosting).setSkills(mockSkills);
        verify(jobPostingRepository).save(jobPosting);
    }
    // 삭제
    @Test
    @DisplayName("deleteJobPosting - 삭제")
    public void givenJobPostingId_whenDelete_thenSucceed(){
        // Given
        Long jobPostingId = 1L;
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(jobPostingId);

        when(jobPostingRepository.findById(Mockito.eq(jobPostingId)))
                .thenReturn(Optional.of(jobPosting));
        // When
        String message = jobPostingService.deleteJobPosting(jobPostingId);
        
        // Then
        assertThat("삭제 되었습니다.").isEqualTo(message);

        // Verify
        verify(jobPostingRepository).delete(jobPosting);
    }

    // 상세 페이지
    @Test
    @DisplayName("findJobPostingDetails - 성공")
    public void givenJobPostingId_whenFind_thenSucceed(){
        // Given
        Long jobPostingId = 1L;
        JobPosting jobPosting = mock(JobPosting.class);
        JobPostingDetails jobPostingDetails = mock(JobPostingDetails.class);
        Company company = mock(Company.class);
        List<JobPosting> sameCompanyJobPostings = Arrays.asList(mock(JobPosting.class), mock(JobPosting.class));

        when(jobPostingRepository.findById(jobPostingId)).thenReturn(Optional.of(jobPosting));
        when(jobPosting.getCompany()).thenReturn(company);
        when(company.getId()).thenReturn(1L);
        when(jobPostingRepository.findByCompanyId(company.getId())).thenReturn(sameCompanyJobPostings);
        when(jobPosting.getJobPostingDetails()).thenReturn(jobPostingDetails);

        // When
        JobPostingDetailResponse response = jobPostingService.findJobPostingDetails(jobPostingId);

        // Then
        assertThat(response).isNotNull();
        assertThat(jobPosting.getId()).isEqualTo(response.jobPostingId());
        assertThat(company.getCompanyName()).isEqualTo(response.companyName());
        assertThat(jobPostingDetails.getDescription()).isEqualTo(response.details());

        // Verify
        verify(jobPostingRepository).findById(jobPostingId);
        verify(jobPostingRepository).findByCompanyId(company.getId());
    }

    // TODO: 검색 기능
}