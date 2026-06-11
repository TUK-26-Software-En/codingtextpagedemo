package com.example.swedemo.problem.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.problem.dto.request.ProblemCreateRequest;
import com.example.swedemo.problem.dto.request.ProblemUpdateRequest;
import com.example.swedemo.problem.dto.response.ProblemResponse;
import com.example.swedemo.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Tag(name = "Problem", description = "문제 관리 API")
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping
    @Operation(summary = "문제 생성")
    public ResponseEntity<ApiResponse<ProblemResponse>> create(@RequestBody ProblemCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(problemService.create(request)));
    }

    @GetMapping
    @Operation(summary = "문제 목록 조회")
    public ResponseEntity<ApiResponse<List<ProblemResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(problemService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "문제 단건 조회")
    public ResponseEntity<ApiResponse<ProblemResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(problemService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "문제 수정")
    public ResponseEntity<ApiResponse<ProblemResponse>> update(@PathVariable Long id,
                                                               @RequestBody ProblemUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(problemService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "문제 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/categories/{categoryId}")
    @Operation(summary = "문제에 카테고리 추가")
    public ResponseEntity<ApiResponse<Void>> addCategory(@PathVariable Long id,
                                                          @PathVariable Long categoryId) {
        problemService.addCategory(id, categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
