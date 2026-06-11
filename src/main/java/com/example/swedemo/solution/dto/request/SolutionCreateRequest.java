package com.example.swedemo.solution.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "풀이 작성 요청")
public class SolutionCreateRequest {

    @Schema(description = "문제 ID", example = "1")
    private Long problemId;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "풀이 제목", example = "DP로 해결하는 방법")
    private String solutionTitle;

    @Schema(description = "풀이 내용")
    private String solutionContent;
}
