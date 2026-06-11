package com.example.swedemo.testcase.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.testcase.dto.request.TestcaseCreateRequest;
import com.example.swedemo.testcase.dto.request.TestcaseUpdateRequest;
import com.example.swedemo.testcase.dto.response.TestcaseResponse;
import com.example.swedemo.testcase.service.TestcaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
@Tag(name = "Testcase", description = "테스트케이스 관리 API")
public class TestcaseController {

    private final TestcaseService testcaseService;

    @PostMapping
    @Operation(summary = "테스트케이스 생성")
    public ResponseEntity<ApiResponse<TestcaseResponse>> create(@RequestBody TestcaseCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(testcaseService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "테스트케이스 단건 조회")
    public ResponseEntity<ApiResponse<TestcaseResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(testcaseService.findById(id)));
    }

    @GetMapping("/problem/{problemId}")
    @Operation(summary = "문제별 테스트케이스 목록 조회")
    public ResponseEntity<ApiResponse<List<TestcaseResponse>>> findByProblemId(@PathVariable Long problemId) {
        return ResponseEntity.ok(ApiResponse.success(testcaseService.findByProblemId(problemId)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "테스트케이스 수정")
    public ResponseEntity<ApiResponse<TestcaseResponse>> update(@PathVariable Long id,
                                                                @RequestBody TestcaseUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(testcaseService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "테스트케이스 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        testcaseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
