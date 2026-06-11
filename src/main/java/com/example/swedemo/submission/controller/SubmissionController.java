package com.example.swedemo.submission.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.submission.dto.request.SubmissionCreateRequest;
import com.example.swedemo.submission.dto.response.SubmissionResponse;
import com.example.swedemo.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Tag(name = "Submission", description = "제출 관리 API")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    @Operation(summary = "코드 제출 (Mock 채점)")
    public ResponseEntity<ApiResponse<SubmissionResponse>> create(@RequestBody SubmissionCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(submissionService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "제출 단건 조회")
    public ResponseEntity<ApiResponse<SubmissionResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(submissionService.findById(id)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 제출 목록 조회")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(submissionService.findByUserId(userId)));
    }

    @GetMapping("/problem/{problemId}")
    @Operation(summary = "문제별 제출 목록 조회")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> findByProblemId(@PathVariable Long problemId) {
        return ResponseEntity.ok(ApiResponse.success(submissionService.findByProblemId(problemId)));
    }
}
