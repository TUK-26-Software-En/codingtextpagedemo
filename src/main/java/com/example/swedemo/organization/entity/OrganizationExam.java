package com.example.swedemo.organization.entity;

import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization_exam")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationExam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationExamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
