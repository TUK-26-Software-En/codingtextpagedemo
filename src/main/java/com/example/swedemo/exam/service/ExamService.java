package com.example.swedemo.exam.service;

import com.example.swedemo.exam.dto.request.ExamCreateRequest;
import com.example.swedemo.exam.dto.request.ExamProblemAddRequest;
import com.example.swedemo.exam.dto.request.ExamUpdateRequest;
import com.example.swedemo.exam.dto.response.ExamResponse;
import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.exam.entity.ExamProblem;
import com.example.swedemo.exam.entity.ExamSupervisor;
import com.example.swedemo.exam.repository.ExamProblemRepository;
import com.example.swedemo.exam.repository.ExamRepository;
import com.example.swedemo.exam.repository.ExamSupervisorRepository;
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
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamProblemRepository examProblemRepository;
    private final ExamSupervisorRepository examSupervisorRepository;
    private final ProblemService problemService;
    private final SupervisorService supervisorService;

    @Transactional
    public ExamResponse create(ExamCreateRequest request) {
        Exam exam = Exam.builder()
                .examTitle(request.getExamTitle())
                .examDescription(request.getExamDescription())
                .examType(request.getExamType())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        return ExamResponse.from(examRepository.save(exam));
    }

    public List<ExamResponse> findAll() {
        return examRepository.findAll().stream()
                .map(ExamResponse::from)
                .collect(Collectors.toList());
    }

    public ExamResponse findById(Long id) {
        return ExamResponse.from(getById(id));
    }

    @Transactional
    public ExamResponse update(Long id, ExamUpdateRequest request) {
        Exam exam = getById(id);
        exam.update(request.getExamTitle(), request.getExamDescription(),
                request.getExamType(), request.getStartTime(), request.getEndTime());
        return ExamResponse.from(exam);
    }

    @Transactional
    public void delete(Long id) {
        examRepository.delete(getById(id));
    }

    @Transactional
    public void addProblem(Long examId, ExamProblemAddRequest request) {
        Exam exam = getById(examId);
        Problem problem = problemService.getById(request.getProblemId());
        ExamProblem examProblem = ExamProblem.builder()
                .exam(exam)
                .problem(problem)
                .problemOrder(request.getProblemOrder())
                .examProblemScore(request.getExamProblemScore())
                .build();
        examProblemRepository.save(examProblem);
    }

    @Transactional
    public void addSupervisor(Long examId, Long supervisorId) {
        Exam exam = getById(examId);
        Supervisor supervisor = supervisorService.getById(supervisorId);
        ExamSupervisor examSupervisor = ExamSupervisor.builder()
                .exam(exam)
                .supervisor(supervisor)
                .build();
        examSupervisorRepository.save(examSupervisor);
    }

    public Exam getById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXAM_NOT_FOUND));
    }
}
