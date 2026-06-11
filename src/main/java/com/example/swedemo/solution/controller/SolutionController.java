package com.example.swedemo.solution.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.solution.dto.request.SolutionCreateRequest;
import com.example.swedemo.solution.dto.request.SolutionUpdateRequest;
import com.example.swedemo.solution.dto.response.SolutionResponse;
import com.example.swedemo.solution.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solutions")
@RequiredArgsConstructor
@Tag(name = "Solution", description = "풀이 관리 API")
public class SolutionController {

    private final SolutionService solutionService;

    @PostMapping
    @Operation(summary = "풀이 작성")
    public ResponseEntity<ApiResponse<SolutionResponse>> create(@RequestBody SolutionCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(solutionService.create(request)));
    }

    @GetMapping
    @Operation(summary = "풀이 목록 조회")
    public ResponseEntity<ApiResponse<List<SolutionResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(solutionService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "풀이 단건 조회")
    public ResponseEntity<ApiResponse<SolutionResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(solutionService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "풀이 수정")
    public ResponseEntity<ApiResponse<SolutionResponse>> update(@PathVariable Long id,
                                                                @RequestBody SolutionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(solutionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "풀이 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        solutionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
