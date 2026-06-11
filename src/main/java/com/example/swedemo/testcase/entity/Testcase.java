package com.example.swedemo.testcase.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "testcase")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Testcase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testcaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(columnDefinition = "TEXT")
    private String inputData;

    @Column(columnDefinition = "TEXT")
    private String outputData;

    public void update(String inputData, String outputData) {
        this.inputData = inputData;
        this.outputData = outputData;
    }
}
