package com.example.swedemo.provider.entity;

import com.example.swedemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;

    @Column(nullable = false, unique = true)
    private String provider;
}
