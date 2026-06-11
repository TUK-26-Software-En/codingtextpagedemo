package com.example.swedemo.judge;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import org.springframework.stereotype.Service;

@Service
public class FakeJudgeService implements JudgeService {

    @Override
    public JudgeResult judge(Long problemId, Language language) {
        return JudgeResult.builder()
                .status(SubmissionStatus.AC)
                .score(100)
                .build();
    }
}
