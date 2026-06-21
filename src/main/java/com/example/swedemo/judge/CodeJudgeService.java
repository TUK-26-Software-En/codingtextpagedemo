package com.example.swedemo.judge;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.SubmissionStatus;
import com.example.swedemo.judge.runner.ExecResult;
import com.example.swedemo.judge.runner.LanguageRunner;
import com.example.swedemo.testcase.entity.Testcase;
import com.example.swedemo.testcase.repository.TestcaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 실제 채점 구현체. 문제의 테스트케이스를 불러와 제출 코드를 언어별 러너로 실행하고,
 * 출력 비교로 채점한다. judge.mode=code 일 때 활성화된다(docker/운영 용).
 *
 * 채점 규칙: 모든 테스트케이스 통과 시 AC/100, 첫 실패에서 해당 상태(WA/TLE/RE/CE)/0 으로 종료.
 */
@Service
@ConditionalOnProperty(name = "judge.mode", havingValue = "code")
public class CodeJudgeService implements JudgeService {

    private static final Logger log = LoggerFactory.getLogger(CodeJudgeService.class);

    private final TestcaseRepository testcaseRepository;
    private final Map<Language, LanguageRunner> runners;
    private final long timeLimitMs;

    public CodeJudgeService(TestcaseRepository testcaseRepository,
                            List<LanguageRunner> runnerList,
                            @Value("${judge.time-limit-ms:2000}") long timeLimitMs) {
        this.testcaseRepository = testcaseRepository;
        this.runners = runnerList.stream()
                .collect(Collectors.toMap(LanguageRunner::language, Function.identity()));
        this.timeLimitMs = timeLimitMs;
        log.info("CodeJudgeService active. languages={}, timeLimitMs={}", runners.keySet(), timeLimitMs);
    }

    @Override
    public JudgeResult judge(Long problemId, Language language, String sourceCode) {
        List<Testcase> testcases = testcaseRepository.findByProblemProblemId(problemId);
        if (testcases.isEmpty()) {
            log.warn("No testcases for problem {} -> WA", problemId);
            return verdict(SubmissionStatus.WA);
        }

        LanguageRunner runner = runners.get(language);
        if (runner == null) {
            log.warn("Unsupported language {} -> CE", language);
            return verdict(SubmissionStatus.CE);
        }

        Path workDir = null;
        try {
            workDir = Files.createTempDirectory("judge-");

            try {
                runner.prepare(workDir, sourceCode);
            } catch (CompileException e) {
                return verdict(SubmissionStatus.CE);
            }

            for (Testcase tc : testcases) {
                ExecResult result = runner.run(workDir, tc.getInputData(), timeLimitMs);
                if (result.isTimedOut()) {
                    return verdict(SubmissionStatus.TLE);
                }
                if (result.getExitCode() != 0) {
                    return verdict(SubmissionStatus.RE);
                }
                if (!normalize(result.getStdout()).equals(normalize(tc.getOutputData()))) {
                    return verdict(SubmissionStatus.WA);
                }
            }
            return JudgeResult.builder().status(SubmissionStatus.AC).score(100).build();

        } catch (IOException e) {
            log.error("Judge IO error for problem {}", problemId, e);
            return verdict(SubmissionStatus.RE);
        } finally {
            cleanup(workDir);
        }
    }

    private JudgeResult verdict(SubmissionStatus status) {
        return JudgeResult.builder().status(status).score(0).build();
    }

    /** 출력 비교용 정규화: 양끝 공백 제거 + 개행 통일. */
    private String normalize(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\r\n", "\n").strip();
    }

    private void cleanup(Path dir) {
        if (dir == null) {
            return;
        }
        try (var walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ignored) {
                    // best-effort 정리
                }
            });
        } catch (IOException ignored) {
            // best-effort 정리
        }
    }
}
