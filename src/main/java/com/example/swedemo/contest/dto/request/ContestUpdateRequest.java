package com.example.swedemo.contest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "대회 수정 요청")
public class ContestUpdateRequest {

    @Schema(description = "대회 제목")
    private String contestTitle;

    @Schema(description = "대회 설명")
    private String contestDescription;

    @Schema(description = "시작 시각")
    private LocalDateTime startTime;

    @Schema(description = "종료 시각")
    private LocalDateTime endTime;
}
