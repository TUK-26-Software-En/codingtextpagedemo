package com.example.swedemo.testcase.dto.response;

import com.example.swedemo.testcase.entity.Testcase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "테스트케이스 응답")
public class TestcaseResponse {

    @Schema(description = "테스트케이스 ID")
    private Long testcaseId;

    @Schema(description = "문제 ID")
    private Long problemId;

    @Schema(description = "입력 데이터")
    private String inputData;

    @Schema(description = "출력 데이터")
    private String outputData;

    public static TestcaseResponse from(Testcase testcase) {
        return TestcaseResponse.builder()
                .testcaseId(testcase.getTestcaseId())
                .problemId(testcase.getProblem().getProblemId())
                .inputData(testcase.getInputData())
                .outputData(testcase.getOutputData())
                .build();
    }
}
