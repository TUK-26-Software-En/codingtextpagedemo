package com.example.swedemo.organization.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization_problem")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
