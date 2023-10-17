package com.recruitment.repository;

import com.recruitment.domain.JobApplicationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationHistoryRepository extends JpaRepository<JobApplicationHistory, Long> {
}
