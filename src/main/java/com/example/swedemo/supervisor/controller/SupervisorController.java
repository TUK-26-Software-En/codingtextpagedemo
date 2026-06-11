package com.example.swedemo.supervisor.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.supervisor.dto.request.SupervisorCreateRequest;
import com.example.swedemo.supervisor.dto.response.SupervisorResponse;
import com.example.swedemo.supervisor.service.SupervisorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisors")
@RequiredArgsConstructor
@Tag(name = "Supervisor", description = "감독자 관리 API")
public class SupervisorController {

    private final SupervisorService supervisorService;

    @PostMapping
    @Operation(summary = "감독자 생성")
    public ResponseEntity<ApiResponse<SupervisorResponse>> create(@RequestBody SupervisorCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(supervisorService.create(request)));
    }

    @GetMapping
    @Operation(summary = "감독자 목록 조회")
    public ResponseEntity<ApiResponse<List<SupervisorResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(supervisorService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "감독자 단건 조회")
    public ResponseEntity<ApiResponse<SupervisorResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(supervisorService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "감독자 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        supervisorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
