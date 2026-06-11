package com.example.swedemo.problem.service;

import com.example.swedemo.category.entity.Category;
import com.example.swedemo.category.service.CategoryService;
import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.problem.dto.request.ProblemCreateRequest;
import com.example.swedemo.problem.dto.request.ProblemUpdateRequest;
import com.example.swedemo.problem.dto.response.ProblemResponse;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.problem.entity.ProblemCategory;
import com.example.swedemo.problem.repository.ProblemCategoryRepository;
import com.example.swedemo.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemCategoryRepository problemCategoryRepository;
    private final CategoryService categoryService;

    @Transactional
    public ProblemResponse create(ProblemCreateRequest request) {
        Problem problem = Problem.builder()
                .problemTitle(request.getProblemTitle())
                .problemContent(request.getProblemContent())
                .problemGrade(request.getProblemGrade())
                .problemPoint(request.getProblemPoint())
                .problemLanguage(request.getProblemLanguage())
                .build();
        return ProblemResponse.from(problemRepository.save(problem));
    }

    public List<ProblemResponse> findAll() {
        return problemRepository.findAll().stream()
                .map(ProblemResponse::from)
                .collect(Collectors.toList());
    }

    public ProblemResponse findById(Long id) {
        return ProblemResponse.from(getById(id));
    }

    @Transactional
    public ProblemResponse update(Long id, ProblemUpdateRequest request) {
        Problem problem = getById(id);
        problem.update(request.getProblemTitle(), request.getProblemContent(),
                request.getProblemGrade(), request.getProblemPoint(), request.getProblemLanguage());
        return ProblemResponse.from(problem);
    }

    @Transactional
    public void delete(Long id) {
        problemRepository.delete(getById(id));
    }

    @Transactional
    public void addCategory(Long problemId, Long categoryId) {
        Problem problem = getById(problemId);
        Category category = categoryService.getById(categoryId);
        ProblemCategory problemCategory = ProblemCategory.builder()
                .problem(problem)
                .category(category)
                .build();
        problemCategoryRepository.save(problemCategory);
    }

    public Problem getById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROBLEM_NOT_FOUND));
    }
}
