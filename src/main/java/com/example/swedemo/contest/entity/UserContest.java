package com.example.swedemo.contest.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_contest")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userContestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime joinedAt;

    @Builder.Default
    private int totalScore = 0;

    @Builder.Default
    private int rank = 0;

    @Builder.Default
    private int solvedCount = 0;
}
