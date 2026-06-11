package com.example.swedemo.solution.service;

import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.problem.service.ProblemService;
import com.example.swedemo.solution.dto.request.SolutionCreateRequest;
import com.example.swedemo.solution.dto.request.SolutionUpdateRequest;
import com.example.swedemo.solution.dto.response.SolutionResponse;
import com.example.swedemo.solution.entity.Solution;
import com.example.swedemo.solution.repository.SolutionRepository;
import com.example.swedemo.user.entity.User;
import com.example.swedemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final ProblemService problemService;
    private final UserService userService;

    @Transactional
    public SolutionResponse create(SolutionCreateRequest request) {
        Problem problem = problemService.getById(request.getProblemId());
        User user = userService.getById(request.getUserId());
        Solution solution = Solution.builder()
                .problem(problem)
                .user(user)
                .solutionTitle(request.getSolutionTitle())
                .solutionContent(request.getSolutionContent())
                .build();
        return SolutionResponse.from(solutionRepository.save(solution));
    }

    public List<SolutionResponse> findAll() {
        return solutionRepository.findAll().stream()
                .map(SolutionResponse::from)
                .collect(Collectors.toList());
    }

    public SolutionResponse findById(Long id) {
        return SolutionResponse.from(getById(id));
    }

    @Transactional
    public SolutionResponse update(Long id, SolutionUpdateRequest request) {
        Solution solution = getById(id);
        solution.update(request.getSolutionTitle(), request.getSolutionContent());
        return SolutionResponse.from(solution);
    }

    @Transactional
    public void delete(Long id) {
        solutionRepository.delete(getById(id));
    }

    private Solution getById(Long id) {
        return solutionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SOLUTION_NOT_FOUND));
    }
}
