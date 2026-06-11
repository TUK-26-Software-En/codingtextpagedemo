package com.example.swedemo.submission.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submission")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Enumerated(EnumType.STRING)
    private Language submissionLanguage;

    @Column(columnDefinition = "TEXT")
    private String submittedCode;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus;

    private int submissionScore;

    private LocalDateTime submittedAt;
}
