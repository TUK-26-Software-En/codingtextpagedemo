package com.example.swedemo.submission.dto.request;

import com.example.swedemo.global.common.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "제출 요청")
public class SubmissionCreateRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "문제 ID", example = "1")
    private Long problemId;

    @Schema(description = "제출 언어", example = "JAVA")
    private Language submissionLanguage;

    @Schema(description = "제출 코드", example = "public class Main { ... }")
    private String submittedCode;
}
