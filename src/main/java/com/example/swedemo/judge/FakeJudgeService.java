package com.example.swedemo.judge;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Mock 채점 구현체. 코드를 실행하지 않고 항상 AC/100 을 반환한다.
 * judge.mode 미지정(기본) 또는 judge.mode=fake 일 때 활성화된다(local/test 용).
 */
@Service
@ConditionalOnProperty(name = "judge.mode", havingValue = "fake", matchIfMissing = true)
public class FakeJudgeService implements JudgeService {

    @Override
    public JudgeResult judge(Long problemId, Language language, String sourceCode) {
        return JudgeResult.builder()
                .status(SubmissionStatus.AC)
                .score(100)
                .build();
    }
}
