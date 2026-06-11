package com.example.swedemo.organization.service;

import com.example.swedemo.contest.entity.Contest;
import com.example.swedemo.contest.service.ContestService;
import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.exam.service.ExamService;
import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.organization.dto.request.OrganizationCreateRequest;
import com.example.swedemo.organization.dto.request.OrganizationUpdateRequest;
import com.example.swedemo.organization.dto.response.OrganizationResponse;
import com.example.swedemo.organization.entity.Organization;
import com.example.swedemo.organization.entity.OrganizationContest;
import com.example.swedemo.organization.entity.OrganizationExam;
import com.example.swedemo.organization.entity.OrganizationProblem;
import com.example.swedemo.organization.repository.*;
import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationProblemRepository organizationProblemRepository;
    private final OrganizationContestRepository organizationContestRepository;
    private final OrganizationExamRepository organizationExamRepository;
    private final ProblemService problemService;
    private final ContestService contestService;
    private final ExamService examService;

    @Transactional
    public OrganizationResponse create(OrganizationCreateRequest request) {
        Organization organization = Organization.builder()
                .organizationName(request.getOrganizationName())
                .organizationDescription(request.getOrganizationDescription())
                .build();
        return OrganizationResponse.from(organizationRepository.save(organization));
    }

    public List<OrganizationResponse> findAll() {
        return organizationRepository.findAll().stream()
                .map(OrganizationResponse::from)
                .collect(Collectors.toList());
    }

    public OrganizationResponse findById(Long id) {
        return OrganizationResponse.from(getById(id));
    }

    @Transactional
    public OrganizationResponse update(Long id, OrganizationUpdateRequest request) {
        Organization organization = getById(id);
        organization.update(request.getOrganizationName(), request.getOrganizationDescription());
        return OrganizationResponse.from(organization);
    }

    @Transactional
    public void delete(Long id) {
        organizationRepository.delete(getById(id));
    }

    @Transactional
    public void addProblem(Long organizationId, Long problemId) {
        Organization organization = getById(organizationId);
        Problem problem = problemService.getById(problemId);
        organizationProblemRepository.save(OrganizationProblem.builder()
                .organization(organization).problem(problem).build());
    }

    @Transactional
    public void addContest(Long organizationId, Long contestId) {
        Organization organization = getById(organizationId);
        Contest contest = contestService.getById(contestId);
        organizationContestRepository.save(OrganizationContest.builder()
                .organization(organization).contest(contest).build());
    }

    @Transactional
    public void addExam(Long organizationId, Long examId) {
        Organization organization = getById(organizationId);
        Exam exam = examService.getById(examId);
        organizationExamRepository.save(OrganizationExam.builder()
                .organization(organization).exam(exam).build());
    }

    private Organization getById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }
}
