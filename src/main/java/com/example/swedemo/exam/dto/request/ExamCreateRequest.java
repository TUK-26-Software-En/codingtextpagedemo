package com.example.swedemo.exam.dto.request;

import com.example.swedemo.global.common.enums.ExamType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "시험 생성 요청")
public class ExamCreateRequest {

    @Schema(description = "시험 제목", example = "2025 정보처리기사 모의고사")
    private String examTitle;

    @Schema(description = "시험 설명")
    private String examDescription;

    @Schema(description = "시험 유형", example = "CERTIFICATION")
    private ExamType examType;

    @Schema(description = "시작 시각")
    private LocalDateTime startTime;

    @Schema(description = "종료 시각")
    private LocalDateTime endTime;
}
