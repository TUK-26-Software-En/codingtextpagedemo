package com.example.swedemo.judge.runner;

import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.judge.CompileException;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

/**
 * Java 러너. 제출 코드는 public class Main 을 포함해야 한다(진입점 규약).
 * prepare 단계에서 javac 로 컴파일하고, 실패 시 CompileException(=CE)으로 변환된다.
 */
@Component
public class JavaRunner extends AbstractProcessRunner {

    @Override
    public Language language() {
        return Language.JAVA;
    }

    @Override
    public void prepare(Path dir, String source) throws CompileException {
        writeSource(dir, "Main.java", source);
        compile(dir, List.of("javac", "Main.java"));
    }

    @Override
    public ExecResult run(Path dir, String input, long timeoutMs) {
        return execute(dir, List.of("java", "-cp", ".", "Main"), input, timeoutMs);
    }
}
