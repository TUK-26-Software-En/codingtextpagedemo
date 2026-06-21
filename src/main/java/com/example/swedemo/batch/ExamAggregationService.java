package com.example.swedemo.batch;

import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.exam.entity.ExamProblem;
import com.example.swedemo.exam.entity.UserExam;
import com.example.swedemo.exam.repository.ExamProblemRepository;
import com.example.swedemo.exam.repository.ExamRepository;
import com.example.swedemo.exam.repository.UserExamRepository;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.submission.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 종료된 시험을 마감하고 응시자 결과(총점/합격/등급)를 집계한다.
 * 합격: 총점 >= 만점의 60%. 등급: 만점 대비 % A>=90/B>=80/C>=70/D>=60/F.
 */
@Service
@RequiredArgsConstructor
public class ExamAggregationService {

    private static final Logger log = LoggerFactory.getLogger(ExamAggregationService.class);
    private static final int PASS_PERCENT = 60;

    private final ExamRepository examRepository;
    private final ExamProblemRepository examProblemRepository;
    private final UserExamRepository userExamRepository;
    private final SubmissionRepository submissionRepository;

    /** 종료됐으나 미마감인 시험 일괄 집계. 처리한 시험 수 반환. */
    @Transactional
    public int aggregateEndedExams() {
        List<Exam> targets = examRepository.findByEndTimeBeforeAndClosedFalse(LocalDateTime.now());
        for (Exam exam : targets) {
            aggregate(exam);
            exam.close();
        }
        if (!targets.isEmpty()) {
            log.info("Exam aggregation: {} exam(s) closed", targets.size());
        }
        return targets.size();
    }

    private void aggregate(Exam exam) {
        LocalDateTime from = exam.getStartTime() != null ? exam.getStartTime() : LocalDateTime.MIN;
        LocalDateTime to = exam.getEndTime();

        List<ExamProblem> problems = examProblemRepository.findByExamExamId(exam.getExamId());
        List<UserExam> examinees = userExamRepository.findByExamExamId(exam.getExamId());

        int maxScore = problems.stream().mapToInt(ExamProblem::getExamProblemScore).sum();

        for (UserExam examinee : examinees) {
            Long userId = examinee.getUser().getUserId();
            int score = 0;
            for (ExamProblem ep : problems) {
                boolean accepted = submissionRepository
                        .existsByUserUserIdAndProblemProblemIdAndSubmissionStatusAndSubmittedAtBetween(
                                userId, ep.getProblem().getProblemId(), SubmissionStatus.AC, from, to);
                if (accepted) {
                    score += ep.getExamProblemScore();
                }
            }
            int percent = maxScore > 0 ? (score * 100 / maxScore) : 0;
            boolean pass = percent >= PASS_PERCENT;
            examinee.applyResult(score, pass, grade(percent));
        }
    }

    private String grade(int percent) {
        if (percent >= 90) return "A";
        if (percent >= 80) return "B";
        if (percent >= 70) return "C";
        if (percent >= 60) return "D";
        return "F";
    }
}
