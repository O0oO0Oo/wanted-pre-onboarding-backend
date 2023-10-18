package com.recruitment.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
public class JobPostingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_posting_details_id")
    private Long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    @UpdateTimestamp
    private ZonedDateTime lastUpdate;
}