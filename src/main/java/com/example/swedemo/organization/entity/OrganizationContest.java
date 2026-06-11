package com.example.swedemo.organization.entity;

import com.example.swedemo.contest.entity.Contest;
import com.example.swedemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization_contest")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationContest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationContestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;
}
