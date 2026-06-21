package com.example.swedemo.submission.repository;

import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUserUserId(Long userId);
    List<Submission> findByProblemProblemId(Long problemId);

    /** 특정 사용자가 특정 문제를 주어진 기간 안에 해당 상태(예: AC)로 제출했는지. (배치 집계용) */
    boolean existsByUserUserIdAndProblemProblemIdAndSubmissionStatusAndSubmittedAtBetween(
            Long userId, Long problemId, SubmissionStatus submissionStatus,
            LocalDateTime from, LocalDateTime to);
}
