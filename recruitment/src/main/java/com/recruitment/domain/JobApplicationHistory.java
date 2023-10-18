package com.recruitment.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
public class JobApplicationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_application_history_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_resume_id", nullable = false)
    private MemberResume memberResume;
    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @UpdateTimestamp
    private ZonedDateTime lastUpdate;
}