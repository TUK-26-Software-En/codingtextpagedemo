package com.example.swedemo.contest.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.supervisor.entity.Supervisor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contest_supervisor")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestSupervisor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contestSupervisorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
}
