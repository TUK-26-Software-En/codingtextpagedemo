package com.example.swedemo.contest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "대회 참가 요청")
public class ContestJoinRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
}
