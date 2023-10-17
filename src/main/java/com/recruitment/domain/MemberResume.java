package com.recruitment.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class MemberResume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_resume_id", nullable = false)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @OneToMany(mappedBy = "memberResume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplicationHistory> jobApplicationHistory = new ArrayList<>();
    @UpdateTimestamp
    private ZonedDateTime lastUpdate;
}
