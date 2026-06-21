package com.example.swedemo.batch.controller;

import com.example.swedemo.batch.ContestAggregationService;
import com.example.swedemo.batch.ExamAggregationService;
import com.example.swedemo.batch.dto.AggregationResult;
import com.example.swedemo.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 배치 수동 트리거. 스케줄러와 동일한 집계를 즉시 실행한다(운영/검증용, 인증 필요).
 */
@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
@Tag(name = "Batch", description = "배치 집계 API")
public class BatchController {

    private final ContestAggregationService contestAggregationService;
    private final ExamAggregationService examAggregationService;

    @PostMapping("/aggregate")
    @Operation(summary = "종료된 대회/시험 집계", description = "endTime이 지난 미마감 대회/시험을 마감하고 결과를 집계한다.")
    public ResponseEntity<ApiResponse<AggregationResult>> aggregate() {
        int contests = contestAggregationService.aggregateEndedContests();
        int exams = examAggregationService.aggregateEndedExams();
        return ResponseEntity.ok(ApiResponse.success(new AggregationResult(contests, exams)));
    }
}
