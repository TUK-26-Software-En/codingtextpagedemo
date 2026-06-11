package com.example.swedemo.contest.controller;

import com.example.swedemo.contest.dto.request.ContestCreateRequest;
import com.example.swedemo.contest.dto.request.ContestJoinRequest;
import com.example.swedemo.contest.dto.request.ContestProblemAddRequest;
import com.example.swedemo.contest.dto.request.ContestUpdateRequest;
import com.example.swedemo.contest.dto.response.ContestResponse;
import com.example.swedemo.contest.dto.response.UserContestResponse;
import com.example.swedemo.contest.facade.ContestFacade;
import com.example.swedemo.contest.service.ContestService;
import com.example.swedemo.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
@Tag(name = "Contest", description = "대회 관리 API")
public class ContestController {

    private final ContestService contestService;
    private final ContestFacade contestFacade;

    @PostMapping
    @Operation(summary = "대회 생성")
    public ResponseEntity<ApiResponse<ContestResponse>> create(@RequestBody ContestCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(contestService.create(request)));
    }

    @GetMapping
    @Operation(summary = "대회 목록 조회")
    public ResponseEntity<ApiResponse<List<ContestResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(contestService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "대회 단건 조회")
    public ResponseEntity<ApiResponse<ContestResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(contestService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "대회 수정")
    public ResponseEntity<ApiResponse<ContestResponse>> update(@PathVariable Long id,
                                                               @RequestBody ContestUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(contestService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "대회 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        contestService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/problems")
    @Operation(summary = "대회에 문제 추가")
    public ResponseEntity<ApiResponse<Void>> addProblem(@PathVariable Long id,
                                                         @RequestBody ContestProblemAddRequest request) {
        contestService.addProblem(id, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/supervisors/{supervisorId}")
    @Operation(summary = "대회에 감독자 추가")
    public ResponseEntity<ApiResponse<Void>> addSupervisor(@PathVariable Long id,
                                                             @PathVariable Long supervisorId) {
        contestService.addSupervisor(id, supervisorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/join")
    @Operation(summary = "대회 참가 신청 (중복 불가)")
    public ResponseEntity<ApiResponse<UserContestResponse>> join(@PathVariable Long id,
                                                                  @RequestBody ContestJoinRequest request) {
        return ResponseEntity.ok(ApiResponse.success(contestFacade.joinContest(id, request)));
    }

    @GetMapping("/{id}/participants")
    @Operation(summary = "대회 참가자 목록 조회")
    public ResponseEntity<ApiResponse<List<UserContestResponse>>> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(contestFacade.getParticipants(id)));
    }
}
