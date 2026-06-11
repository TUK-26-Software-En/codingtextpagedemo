package com.example.swedemo.category.controller;

import com.example.swedemo.category.dto.request.CategoryCreateRequest;
import com.example.swedemo.category.dto.response.CategoryResponse;
import com.example.swedemo.category.service.CategoryService;
import com.example.swedemo.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.create(request)));
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "카테고리 단건 조회")
    public ResponseEntity<ApiResponse<CategoryResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
