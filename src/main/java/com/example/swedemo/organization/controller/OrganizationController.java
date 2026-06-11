package com.example.swedemo.organization.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.organization.dto.request.OrganizationCreateRequest;
import com.example.swedemo.organization.dto.request.OrganizationUpdateRequest;
import com.example.swedemo.organization.dto.response.OrganizationResponse;
import com.example.swedemo.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Tag(name = "Organization", description = "기관 관리 API")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @Operation(summary = "기관 생성")
    public ResponseEntity<ApiResponse<OrganizationResponse>> create(@RequestBody OrganizationCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.create(request)));
    }

    @GetMapping
    @Operation(summary = "기관 목록 조회")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(organizationService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "기관 단건 조회")
    public ResponseEntity<ApiResponse<OrganizationResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "기관 수정")
    public ResponseEntity<ApiResponse<OrganizationResponse>> update(@PathVariable Long id,
                                                                     @RequestBody OrganizationUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(organizationService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "기관 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/problems/{problemId}")
    @Operation(summary = "기관에 문제 등록")
    public ResponseEntity<ApiResponse<Void>> addProblem(@PathVariable Long id, @PathVariable Long problemId) {
        organizationService.addProblem(id, problemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/contests/{contestId}")
    @Operation(summary = "기관에 대회 등록")
    public ResponseEntity<ApiResponse<Void>> addContest(@PathVariable Long id, @PathVariable Long contestId) {
        organizationService.addContest(id, contestId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/exams/{examId}")
    @Operation(summary = "기관에 시험 등록")
    public ResponseEntity<ApiResponse<Void>> addExam(@PathVariable Long id, @PathVariable Long examId) {
        organizationService.addExam(id, examId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
