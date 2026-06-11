package com.example.swedemo.testcase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "테스트케이스 생성 요청")
public class TestcaseCreateRequest {

    @Schema(description = "문제 ID", example = "1")
    private Long problemId;

    @Schema(description = "입력 데이터", example = "1 2")
    private String inputData;

    @Schema(description = "출력 데이터", example = "3")
    private String outputData;
}
