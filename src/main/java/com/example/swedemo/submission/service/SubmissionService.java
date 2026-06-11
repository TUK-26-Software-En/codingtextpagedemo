package com.example.swedemo.submission.service;

import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.judge.JudgeResult;
import com.example.swedemo.judge.JudgeService;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.problem.service.ProblemService;
import com.example.swedemo.submission.dto.request.SubmissionCreateRequest;
import com.example.swedemo.submission.dto.response.SubmissionResponse;
import com.example.swedemo.submission.entity.Submission;
import com.example.swedemo.submission.repository.SubmissionRepository;
import com.example.swedemo.user.entity.User;
import com.example.swedemo.user.entity.UserProblem;
import com.example.swedemo.user.repository.UserProblemRepository;
import com.example.swedemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserProblemRepository userProblemRepository;
    private final UserService userService;
    private final ProblemService problemService;
    private final JudgeService judgeService;

    @Transactional
    public SubmissionResponse create(SubmissionCreateRequest request) {
        User user = userService.getById(request.getUserId());
        Problem problem = problemService.getById(request.getProblemId());

        JudgeResult result = judgeService.judge(problem.getProblemId(), request.getSubmissionLanguage());

        Submission submission = Submission.builder()
                .user(user)
                .problem(problem)
                .submissionLanguage(request.getSubmissionLanguage())
                .submittedCode(request.getSubmittedCode())
                .submissionStatus(result.getStatus())
                .submissionScore(result.getScore())
                .submittedAt(LocalDateTime.now())
                .build();

        submissionRepository.save(submission);
        updateUserProblem(user, problem, result);

        return SubmissionResponse.from(submission);
    }

    public SubmissionResponse findById(Long id) {
        return SubmissionResponse.from(getById(id));
    }

    public List<SubmissionResponse> findByUserId(Long userId) {
        return submissionRepository.findByUserUserId(userId).stream()
                .map(SubmissionResponse::from)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponse> findByProblemId(Long problemId) {
        return submissionRepository.findByProblemProblemId(problemId).stream()
                .map(SubmissionResponse::from)
                .collect(Collectors.toList());
    }

    private void updateUserProblem(User user, Problem problem, JudgeResult result) {
        Optional<UserProblem> existing = userProblemRepository.findByUserAndProblem(user, problem);
        if (existing.isPresent()) {
            existing.get().updateStatus(result.getStatus());
        } else {
            UserProblem userProblem = UserProblem.builder()
                    .user(user)
                    .problem(problem)
                    .userProblemStatus(result.getStatus())
                    .build();
            userProblemRepository.save(userProblem);
            if (result.getStatus().name().equals("AC")) {
                userProblem.updateStatus(result.getStatus());
            }
        }
    }

    private Submission getById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBMISSION_NOT_FOUND));
    }
}
