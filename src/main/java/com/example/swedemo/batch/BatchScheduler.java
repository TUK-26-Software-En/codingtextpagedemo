package com.example.swedemo.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 주기 배치. 종료된 대회/시험을 자동 마감·집계한다.
 */
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final ContestAggregationService contestAggregationService;
    private final ExamAggregationService examAggregationService;

    @Scheduled(
            fixedDelayString = "${batch.aggregate-interval-ms:60000}",
            initialDelayString = "${batch.initial-delay-ms:10000}")
    public void aggregate() {
        contestAggregationService.aggregateEndedContests();
        examAggregationService.aggregateEndedExams();
    }
}
