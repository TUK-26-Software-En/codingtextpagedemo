package com.example.swedemo.provider.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.provider.dto.request.ProviderCreateRequest;
import com.example.swedemo.provider.dto.response.ProviderResponse;
import com.example.swedemo.provider.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Tag(name = "Provider", description = "제공자 관리 API")
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    @Operation(summary = "제공자 생성")
    public ResponseEntity<ApiResponse<ProviderResponse>> create(@RequestBody ProviderCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(providerService.create(request)));
    }

    @GetMapping
    @Operation(summary = "제공자 목록 조회")
    public ResponseEntity<ApiResponse<List<ProviderResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(providerService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "제공자 단건 조회")
    public ResponseEntity<ApiResponse<ProviderResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(providerService.findById(id)));
    }
}
