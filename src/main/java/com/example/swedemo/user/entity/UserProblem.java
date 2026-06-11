package com.example.swedemo.user.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_problem")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus userProblemStatus;

    private LocalDateTime solvedAt;

    public void updateStatus(SubmissionStatus status) {
        this.userProblemStatus = status;
        if (status == SubmissionStatus.AC) {
            this.solvedAt = LocalDateTime.now();
        }
    }
}
