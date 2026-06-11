package com.example.swedemo.contest.service;

import com.example.swedemo.contest.dto.request.ContestCreateRequest;
import com.example.swedemo.contest.dto.request.ContestProblemAddRequest;
import com.example.swedemo.contest.dto.request.ContestUpdateRequest;
import com.example.swedemo.contest.dto.response.ContestResponse;
import com.example.swedemo.contest.entity.Contest;
import com.example.swedemo.contest.entity.ContestProblem;
import com.example.swedemo.contest.entity.ContestSupervisor;
import com.example.swedemo.contest.repository.ContestProblemRepository;
import com.example.swedemo.contest.repository.ContestRepository;
import com.example.swedemo.contest.repository.ContestSupervisorRepository;
import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.problem.service.ProblemService;
import com.example.swedemo.supervisor.entity.Supervisor;
import com.example.swedemo.supervisor.service.SupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContestService {

    private final ContestRepository contestRepository;
    private final ContestProblemRepository contestProblemRepository;
    private final ContestSupervisorRepository contestSupervisorRepository;
    private final ProblemService problemService;
    private final SupervisorService supervisorService;

    @Transactional
    public ContestResponse create(ContestCreateRequest request) {
        Contest contest = Contest.builder()
                .contestTitle(request.getContestTitle())
                .contestDescription(request.getContestDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        return ContestResponse.from(contestRepository.save(contest));
    }

    public List<ContestResponse> findAll() {
        return contestRepository.findAll().stream()
                .map(ContestResponse::from)
                .collect(Collectors.toList());
    }

    public ContestResponse findById(Long id) {
        return ContestResponse.from(getById(id));
    }

    @Transactional
    public ContestResponse update(Long id, ContestUpdateRequest request) {
        Contest contest = getById(id);
        contest.update(request.getContestTitle(), request.getContestDescription(),
                request.getStartTime(), request.getEndTime());
        return ContestResponse.from(contest);
    }

    @Transactional
    public void delete(Long id) {
        contestRepository.delete(getById(id));
    }

    @Transactional
    public void addProblem(Long contestId, ContestProblemAddRequest request) {
        Contest contest = getById(contestId);
        Problem problem = problemService.getById(request.getProblemId());
        ContestProblem contestProblem = ContestProblem.builder()
                .contest(contest)
                .problem(problem)
                .problemOrder(request.getProblemOrder())
                .contestProblemScore(request.getContestProblemScore())
                .build();
        contestProblemRepository.save(contestProblem);
    }

    @Transactional
    public void addSupervisor(Long contestId, Long supervisorId) {
        Contest contest = getById(contestId);
        Supervisor supervisor = supervisorService.getById(supervisorId);
        ContestSupervisor contestSupervisor = ContestSupervisor.builder()
                .contest(contest)
                .supervisor(supervisor)
                .build();
        contestSupervisorRepository.save(contestSupervisor);
    }

    public Contest getById(Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONTEST_NOT_FOUND));
    }
}
