package com.example.swedemo.exam.dto.response;

import com.example.swedemo.exam.entity.UserExam;
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
@Schema(description = "시험 응시 결과 응답")
public class UserExamResponse {

    @Schema(description = "응시 ID")
    private Long userExamId;

    @Schema(description = "시험 ID")
    private Long examId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "응시 시각")
    private LocalDateTime joinedAt;

    @Schema(description = "총 점수")
    private int totalScore;

    @Schema(description = "합격 여부")
    private boolean passStatus;

    @Schema(description = "등급")
    private String examGrade;

    public static UserExamResponse from(UserExam userExam) {
        return UserExamResponse.builder()
                .userExamId(userExam.getUserExamId())
                .examId(userExam.getExam().getExamId())
                .userId(userExam.getUser().getUserId())
                .userName(userExam.getUser().getUserName())
                .joinedAt(userExam.getJoinedAt())
                .totalScore(userExam.getTotalScore())
                .passStatus(userExam.isPassStatus())
                .examGrade(userExam.getExamGrade())
                .build();
    }
}
