package com.example.swedemo.judge;

import com.example.swedemo.global.common.enums.Language;

/**
 * 채점 전략 공통 인터페이스(Strategy). 구현체는 FakeJudgeService(Mock) 또는 CodeJudgeService(실제 실행)이며,
 * judge.mode 설정으로 택일된다. 제출 코드 본문(sourceCode)을 받아 실제 채점이 가능하도록 한다.
 */
public interface JudgeService {

    JudgeResult judge(Long problemId, Language language, String sourceCode);
}
