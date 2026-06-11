package com.example.swedemo.problem.entity;

import com.example.swedemo.category.entity.Category;
import com.example.swedemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "problem_category")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
