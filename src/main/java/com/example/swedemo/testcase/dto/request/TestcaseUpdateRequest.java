package com.example.swedemo.testcase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "테스트케이스 수정 요청")
public class TestcaseUpdateRequest {

    @Schema(description = "입력 데이터")
    private String inputData;

    @Schema(description = "출력 데이터")
    private String outputData;
}
