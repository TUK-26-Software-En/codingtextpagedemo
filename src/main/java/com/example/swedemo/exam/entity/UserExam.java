package com.example.swedemo.exam.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_exam")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userExamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime joinedAt;

    @Builder.Default
    private int totalScore = 0;

    @Builder.Default
    private boolean passStatus = false;

    private String examGrade;
}
