package com.example.swedemo.solution.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "solution")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long solutionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String solutionTitle;

    @Column(columnDefinition = "TEXT")
    private String solutionContent;

    public void update(String solutionTitle, String solutionContent) {
        this.solutionTitle = solutionTitle;
        this.solutionContent = solutionContent;
    }
}
