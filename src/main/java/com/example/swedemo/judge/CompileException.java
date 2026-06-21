package com.example.swedemo.judge;

/**
 * 제출 코드 컴파일 실패를 나타내는 예외. CodeJudgeService에서 CE(Compile Error)로 변환된다.
 */
public class CompileException extends Exception {

    public CompileException(String message) {
        super(message);
    }
}
