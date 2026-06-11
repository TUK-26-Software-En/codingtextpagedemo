package com.example.swedemo.judge;

import com.example.swedemo.global.common.enums.Language;

public interface JudgeService {
    JudgeResult judge(Long problemId, Language language);
}
