package com.example.swedemo.problem.dto.request;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.Rank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "문제 생성 요청")
public class ProblemCreateRequest {

    @Schema(description = "문제 제목", example = "두 수의 합")
    private String problemTitle;

    @Schema(description = "문제 내용")
    private String problemContent;

    @Schema(description = "난이도", example = "SILVER")
    private Rank problemGrade;

    @Schema(description = "포인트", example = "100")
    private int problemPoint;

    @Schema(description = "언어", example = "JAVA")
    private Language problemLanguage;
}
