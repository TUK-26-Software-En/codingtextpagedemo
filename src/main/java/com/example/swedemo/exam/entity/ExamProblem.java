package com.example.swedemo.exam.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_problem")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private int problemOrder;
    private int examProblemScore;
}
