package com.example.swedemo.batch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 배치 집계 결과 요약.
 */
@Getter
@AllArgsConstructor
@Schema(description = "배치 집계 결과")
public class AggregationResult {

    @Schema(description = "마감된 대회 수")
    private int contestsClosed;

    @Schema(description = "마감된 시험 수")
    private int examsClosed;
}
