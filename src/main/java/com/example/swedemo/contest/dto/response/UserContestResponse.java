package com.example.swedemo.contest.dto.response;

import com.example.swedemo.contest.entity.UserContest;
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
@Schema(description = "대회 참가 응답")
public class UserContestResponse {

    @Schema(description = "참가 ID")
    private Long userContestId;

    @Schema(description = "대회 ID")
    private Long contestId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "참가 시각")
    private LocalDateTime joinedAt;

    @Schema(description = "총 점수")
    private int totalScore;

    @Schema(description = "순위")
    private int rank;

    @Schema(description = "해결 문제 수")
    private int solvedCount;

    public static UserContestResponse from(UserContest userContest) {
        return UserContestResponse.builder()
                .userContestId(userContest.getUserContestId())
                .contestId(userContest.getContest().getContestId())
                .userId(userContest.getUser().getUserId())
                .userName(userContest.getUser().getUserName())
                .joinedAt(userContest.getJoinedAt())
                .totalScore(userContest.getTotalScore())
                .rank(userContest.getRank())
                .solvedCount(userContest.getSolvedCount())
                .build();
    }
}
