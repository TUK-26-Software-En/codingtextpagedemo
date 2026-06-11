package com.example.swedemo.exam.dto.response;

import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.global.common.enums.ExamType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "시험 응답")
public class ExamResponse {

    @Schema(description = "시험 ID")
    private Long examId;

    @Schema(description = "시험 제목")
    private String examTitle;

    @Schema(description = "시험 설명")
    private String examDescription;

    @Schema(description = "시험 유형")
    private ExamType examType;

    @Schema(description = "시작 시각")
    private LocalDateTime startTime;

    @Schema(description = "종료 시각")
    private LocalDateTime endTime;

    public static ExamResponse from(Exam exam) {
        return ExamResponse.builder()
                .examId(exam.getExamId())
                .examTitle(exam.getExamTitle())
                .examDescription(exam.getExamDescription())
                .examType(exam.getExamType())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .build();
    }
}
