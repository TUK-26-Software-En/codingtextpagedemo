package com.example.swedemo.testcase.service;

import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.problem.service.ProblemService;
import com.example.swedemo.testcase.dto.request.TestcaseCreateRequest;
import com.example.swedemo.testcase.dto.request.TestcaseUpdateRequest;
import com.example.swedemo.testcase.dto.response.TestcaseResponse;
import com.example.swedemo.testcase.entity.Testcase;
import com.example.swedemo.testcase.repository.TestcaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestcaseService {

    private final TestcaseRepository testcaseRepository;
    private final ProblemService problemService;

    @Transactional
    public TestcaseResponse create(TestcaseCreateRequest request) {
        Problem problem = problemService.getById(request.getProblemId());
        Testcase testcase = Testcase.builder()
                .problem(problem)
                .inputData(request.getInputData())
                .outputData(request.getOutputData())
                .build();
        return TestcaseResponse.from(testcaseRepository.save(testcase));
    }

    public TestcaseResponse findById(Long id) {
        return TestcaseResponse.from(getById(id));
    }

    public List<TestcaseResponse> findByProblemId(Long problemId) {
        return testcaseRepository.findByProblemProblemId(problemId).stream()
                .map(TestcaseResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TestcaseResponse update(Long id, TestcaseUpdateRequest request) {
        Testcase testcase = getById(id);
        testcase.update(request.getInputData(), request.getOutputData());
        return TestcaseResponse.from(testcase);
    }

    @Transactional
    public void delete(Long id) {
        testcaseRepository.delete(getById(id));
    }

    private Testcase getById(Long id) {
        return testcaseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TESTCASE_NOT_FOUND));
    }
}
