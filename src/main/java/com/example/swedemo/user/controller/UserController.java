package com.example.swedemo.user.controller;

import com.example.swedemo.global.common.ApiResponse;
import com.example.swedemo.user.dto.request.UserCreateRequest;
import com.example.swedemo.user.dto.request.UserUpdateRequest;
import com.example.swedemo.user.dto.response.UserResponse;
import com.example.swedemo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "사용자 생성")
    public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.create(request)));
    }

    @GetMapping
    @Operation(summary = "사용자 목록 조회")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 단건 조회")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
