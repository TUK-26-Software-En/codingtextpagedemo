package com.example.swedemo.judge;

import com.example.swedemo.global.common.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JudgeResult {
    private SubmissionStatus status;
    private int score;
}
