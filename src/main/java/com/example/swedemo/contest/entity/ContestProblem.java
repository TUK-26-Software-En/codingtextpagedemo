package com.example.swedemo.contest.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contest_problem")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contestProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private int problemOrder;
    private int contestProblemScore;
}
