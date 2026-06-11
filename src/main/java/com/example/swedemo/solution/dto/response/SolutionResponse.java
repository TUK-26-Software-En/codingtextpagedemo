package com.example.swedemo.solution.dto.response;

import com.example.swedemo.solution.entity.Solution;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "풀이 응답")
public class SolutionResponse {

    @Schema(description = "풀이 ID")
    private Long solutionId;

    @Schema(description = "문제 ID")
    private Long problemId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "풀이 제목")
    private String solutionTitle;

    @Schema(description = "풀이 내용")
    private String solutionContent;

    public static SolutionResponse from(Solution solution) {
        return SolutionResponse.builder()
                .solutionId(solution.getSolutionId())
                .problemId(solution.getProblem().getProblemId())
                .userId(solution.getUser().getUserId())
                .userName(solution.getUser().getUserName())
                .solutionTitle(solution.getSolutionTitle())
                .solutionContent(solution.getSolutionContent())
                .build();
    }
}
