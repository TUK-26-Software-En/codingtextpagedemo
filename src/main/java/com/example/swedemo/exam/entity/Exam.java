package com.example.swedemo.exam.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.global.common.enums.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @Column(nullable = false)
    private String examTitle;

    @Column(columnDefinition = "TEXT")
    private String examDescription;

    @Enumerated(EnumType.STRING)
    private ExamType examType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /** 배치 집계 완료(마감) 여부. 멱등 처리를 위해 사용. */
    @Builder.Default
    private boolean closed = false;

    public void update(String examTitle, String examDescription, ExamType examType,
                       LocalDateTime startTime, LocalDateTime endTime) {
        this.examTitle = examTitle;
        this.examDescription = examDescription;
        this.examType = examType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void close() {
        this.closed = true;
    }
}
