package com.example.swedemo.contest.entity;

import com.example.swedemo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contest")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contestId;

    @Column(nullable = false)
    private String contestTitle;

    @Column(columnDefinition = "TEXT")
    private String contestDescription;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void update(String contestTitle, String contestDescription,
                       LocalDateTime startTime, LocalDateTime endTime) {
        this.contestTitle = contestTitle;
        this.contestDescription = contestDescription;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
