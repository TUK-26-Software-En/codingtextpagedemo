package com.example.swedemo.exam.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "시험 응시 요청")
public class ExamTakeRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
}
