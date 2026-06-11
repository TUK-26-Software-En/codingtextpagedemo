package com.example.swedemo.problem.dto.request;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.Rank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "문제 수정 요청")
public class ProblemUpdateRequest {

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
}
