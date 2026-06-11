package com.example.swedemo.solution.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "풀이 수정 요청")
public class SolutionUpdateRequest {

    @Schema(description = "풀이 제목")
    private String solutionTitle;

    @Schema(description = "풀이 내용")
    private String solutionContent;
}
