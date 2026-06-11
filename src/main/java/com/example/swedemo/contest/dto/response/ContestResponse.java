package com.example.swedemo.contest.dto.response;

import com.example.swedemo.contest.entity.Contest;
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
@Schema(description = "대회 응답")
public class ContestResponse {

    @Schema(description = "대회 ID")
    private Long contestId;

    @Schema(description = "대회 제목")
    private String contestTitle;

    @Schema(description = "대회 설명")
    private String contestDescription;

    @Schema(description = "시작 시각")
    private LocalDateTime startTime;

    @Schema(description = "종료 시각")
    private LocalDateTime endTime;

    public static ContestResponse from(Contest contest) {
        return ContestResponse.builder()
                .contestId(contest.getContestId())
                .contestTitle(contest.getContestTitle())
                .contestDescription(contest.getContestDescription())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .build();
    }
}
