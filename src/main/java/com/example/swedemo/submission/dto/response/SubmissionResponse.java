package com.example.swedemo.submission.dto.response;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.submission.entity.Submission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "제출 응답")
public class SubmissionResponse {

    @Schema(description = "제출 ID")
    private Long submissionId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "문제 ID")
    private Long problemId;

    @Schema(description = "제출 언어")
    private Language submissionLanguage;

    @Schema(description = "제출 코드")
    private String submittedCode;

    @Schema(description = "채점 결과")
    private SubmissionStatus submissionStatus;

    @Schema(description = "점수")
    private int submissionScore;

    @Schema(description = "제출 시각")
    private LocalDateTime submittedAt;

    public static SubmissionResponse from(Submission submission) {
        return SubmissionResponse.builder()
                .submissionId(submission.getSubmissionId())
                .userId(submission.getUser().getUserId())
                .problemId(submission.getProblem().getProblemId())
                .submissionLanguage(submission.getSubmissionLanguage())
                .submittedCode(submission.getSubmittedCode())
                .submissionStatus(submission.getSubmissionStatus())
                .submissionScore(submission.getSubmissionScore())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
}
