package com.example.swedemo.judge.runner;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.judge.CompileException;

import java.nio.file.Path;

/**
 * 언어별 실행 전략(Strategy). 컴파일 언어와 인터프리터 언어를 동일 인터페이스로 추상화한다.
 */
public interface LanguageRunner {

    /** 이 러너가 담당하는 언어 */
    Language language();

    /** 작업 디렉터리에 소스를 작성하고, 컴파일 언어라면 컴파일한다. 실패 시 CompileException. */
    void prepare(Path dir, String source) throws CompileException;

    /** 준비된 프로그램을 입력(stdin)과 함께 1회 실행한다. */
    ExecResult run(Path dir, String input, long timeoutMs);
}
