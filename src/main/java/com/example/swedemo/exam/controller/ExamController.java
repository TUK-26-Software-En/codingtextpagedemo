package com.example.swedemo.exam.controller;

import com.example.swedemo.exam.dto.request.ExamCreateRequest;
import com.example.swedemo.exam.dto.request.ExamProblemAddRequest;
import com.example.swedemo.exam.dto.request.ExamTakeRequest;
import com.example.swedemo.exam.dto.request.ExamUpdateRequest;
import com.example.swedemo.exam.dto.response.ExamResponse;
import com.example.swedemo.exam.dto.response.UserExamResponse;
import com.example.swedemo.exam.facade.ExamFacade;
import com.example.swedemo.exam.service.ExamService;
import com.example.swedemo.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Tag(name = "Exam", description = "시험 관리 API")
public class ExamController {

    private final ExamService examService;
    private final ExamFacade examFacade;

    @PostMapping
    @Operation(summary = "시험 생성")
    public ResponseEntity<ApiResponse<ExamResponse>> create(@RequestBody ExamCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(examService.create(request)));
    }

    @GetMapping
    @Operation(summary = "시험 목록 조회")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(examService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "시험 단건 조회")
    public ResponseEntity<ApiResponse<ExamResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(examService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "시험 수정")
    public ResponseEntity<ApiResponse<ExamResponse>> update(@PathVariable Long id,
                                                             @RequestBody ExamUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(examService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "시험 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        examService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/problems")
    @Operation(summary = "시험에 문제 추가")
    public ResponseEntity<ApiResponse<Void>> addProblem(@PathVariable Long id,
                                                         @RequestBody ExamProblemAddRequest request) {
        examService.addProblem(id, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/supervisors/{supervisorId}")
    @Operation(summary = "시험에 감독자 추가")
    public ResponseEntity<ApiResponse<Void>> addSupervisor(@PathVariable Long id,
                                                             @PathVariable Long supervisorId) {
        examService.addSupervisor(id, supervisorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/take")
    @Operation(summary = "시험 응시 신청 (중복 불가)")
    public ResponseEntity<ApiResponse<UserExamResponse>> take(@PathVariable Long id,
                                                               @RequestBody ExamTakeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(examFacade.takeExam(id, request)));
    }

    @GetMapping("/{id}/results")
    @Operation(summary = "시험 결과 목록 조회")
    public ResponseEntity<ApiResponse<List<UserExamResponse>>> getResults(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(examFacade.getResults(id)));
    }
}
