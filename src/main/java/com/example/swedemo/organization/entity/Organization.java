package com.example.swedemo.organization.entity;

import com.example.swedemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationId;

    @Column(nullable = false)
    private String organizationName;

    @Column(columnDefinition = "TEXT")
    private String organizationDescription;

    public void update(String organizationName, String organizationDescription) {
        this.organizationName = organizationName;
        this.organizationDescription = organizationDescription;
    }
}
