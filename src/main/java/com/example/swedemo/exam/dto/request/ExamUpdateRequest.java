package com.example.swedemo.exam.dto.request;

import com.example.swedemo.global.common.enums.ExamType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "시험 수정 요청")
public class ExamUpdateRequest {

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
}
