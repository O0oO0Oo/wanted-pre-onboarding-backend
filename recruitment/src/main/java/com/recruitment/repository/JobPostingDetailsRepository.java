package com.recruitment.repository;

import com.recruitment.domain.JobPostingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingDetailsRepository extends JpaRepository<JobPostingDetails, Long> {

}
