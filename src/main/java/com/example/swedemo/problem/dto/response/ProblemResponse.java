package com.example.swedemo.problem.dto.response;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.Rank;
import com.example.swedemo.problem.entity.Problem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "문제 응답")
public class ProblemResponse {

    @Schema(description = "문제 ID")
    private Long problemId;

    @Schema(description = "문제 제목")
    private String problemTitle;

    @Schema(description = "문제 내용")
    private String problemContent;

    @Schema(description = "난이도")
    private Rank problemGrade;

    @Schema(description = "포인트")
    private int problemPoint;

    @Schema(description = "언어")
    private Language problemLanguage;

    public static ProblemResponse from(Problem problem) {
        return ProblemResponse.builder()
                .problemId(problem.getProblemId())
                .problemTitle(problem.getProblemTitle())
                .problemContent(problem.getProblemContent())
                .problemGrade(problem.getProblemGrade())
                .problemPoint(problem.getProblemPoint())
                .problemLanguage(problem.getProblemLanguage())
                .build();
    }
}
