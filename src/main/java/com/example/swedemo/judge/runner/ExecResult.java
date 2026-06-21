package com.example.swedemo.judge.runner;

import lombok.Builder;
import lombok.Getter;

/**
 * 프로세스 1회 실행 결과 값 객체.
 */
@Getter
@Builder
public class ExecResult {

    /** 표준 출력(표준 에러 병합) 내용 */
    private final String stdout;

    /** 프로세스 종료 코드 (타임아웃/실행 실패 시 -1) */
    private final int exitCode;

    /** 시간 제한 초과 여부 */
    private final boolean timedOut;
}
