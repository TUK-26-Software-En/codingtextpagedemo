package com.example.swedemo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    SUPERVISOR_NOT_FOUND("감독자를 찾을 수 없습니다."),
    PROVIDER_NOT_FOUND("제공자를 찾을 수 없습니다."),
    ORGANIZATION_NOT_FOUND("기관을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    PROBLEM_NOT_FOUND("문제를 찾을 수 없습니다."),
    TESTCASE_NOT_FOUND("테스트케이스를 찾을 수 없습니다."),
    SUBMISSION_NOT_FOUND("제출을 찾을 수 없습니다."),
    SOLUTION_NOT_FOUND("풀이를 찾을 수 없습니다."),
    CONTEST_NOT_FOUND("대회를 찾을 수 없습니다."),
    EXAM_NOT_FOUND("시험을 찾을 수 없습니다."),
    CONTEST_PROBLEM_NOT_FOUND("대회 문제를 찾을 수 없습니다."),
    EXAM_PROBLEM_NOT_FOUND("시험 문제를 찾을 수 없습니다."),
    DUPLICATE_PARTICIPATION("이미 참가한 대회/시험입니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    UNAUTHORIZED("인증이 필요합니다."),
    FORBIDDEN("권한이 없습니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;
}
