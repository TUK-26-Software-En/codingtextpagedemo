package com.example.swedemo.contest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "대회 문제 추가 요청")
public class ContestProblemAddRequest {

    @Schema(description = "문제 ID", example = "1")
    private Long problemId;

    @Schema(description = "문제 순서", example = "1")
    private int problemOrder;

    @Schema(description = "문제 배점", example = "100")
    private int contestProblemScore;
}
