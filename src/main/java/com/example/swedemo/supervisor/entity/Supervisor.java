package com.example.swedemo.supervisor.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.provider.entity.Provider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supervisor")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supervisor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supervisorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(nullable = false)
    private String supervisorName;
}
