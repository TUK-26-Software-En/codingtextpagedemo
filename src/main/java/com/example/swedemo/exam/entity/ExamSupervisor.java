package com.example.swedemo.exam.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.supervisor.entity.Supervisor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_supervisor")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSupervisor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examSupervisorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
}
