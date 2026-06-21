package com.example.swedemo.batch;

import com.example.swedemo.contest.entity.Contest;
import com.example.swedemo.contest.entity.ContestProblem;
import com.example.swedemo.contest.entity.UserContest;
import com.example.swedemo.contest.repository.ContestProblemRepository;
import com.example.swedemo.contest.repository.ContestRepository;
import com.example.swedemo.contest.repository.UserContestRepository;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.submission.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 종료된 대회를 마감하고 참가자 결과(총점/해결 수/순위)를 집계한다.
 * 집계 기준: 참가자가 대회 문제를 대회 기간 [startTime, endTime] 안에 AC 제출했는가.
 */
@Service
@RequiredArgsConstructor
public class ContestAggregationService {

    private static final Logger log = LoggerFactory.getLogger(ContestAggregationService.class);

    private final ContestRepository contestRepository;
    private final ContestProblemRepository contestProblemRepository;
    private final UserContestRepository userContestRepository;
    private final SubmissionRepository submissionRepository;

    /** 종료됐으나 미마감인 대회 일괄 집계. 처리한 대회 수 반환. */
    @Transactional
    public int aggregateEndedContests() {
        List<Contest> targets = contestRepository.findByEndTimeBeforeAndClosedFalse(LocalDateTime.now());
        for (Contest contest : targets) {
            aggregate(contest);
            contest.close();
        }
        if (!targets.isEmpty()) {
            log.info("Contest aggregation: {} contest(s) closed", targets.size());
        }
        return targets.size();
    }

    private void aggregate(Contest contest) {
        LocalDateTime from = contest.getStartTime() != null ? contest.getStartTime() : LocalDateTime.MIN;
        LocalDateTime to = contest.getEndTime();

        List<ContestProblem> problems = contestProblemRepository.findByContestContestId(contest.getContestId());
        List<UserContest> participants = userContestRepository.findByContestContestId(contest.getContestId());

        for (UserContest participant : participants) {
            Long userId = participant.getUser().getUserId();
            int solved = 0;
            int score = 0;
            for (ContestProblem cp : problems) {
                boolean accepted = submissionRepository
                        .existsByUserUserIdAndProblemProblemIdAndSubmissionStatusAndSubmittedAtBetween(
                                userId, cp.getProblem().getProblemId(), SubmissionStatus.AC, from, to);
                if (accepted) {
                    solved++;
                    score += cp.getContestProblemScore();
                }
            }
            participant.applyResult(score, solved);
        }

        // 총점 내림차순 순위 부여 (동점은 순번)
        participants.sort(Comparator.comparingInt(UserContest::getTotalScore).reversed());
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).assignRank(i + 1);
        }
    }
}
