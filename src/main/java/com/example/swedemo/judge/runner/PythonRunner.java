package com.example.swedemo.judge.runner;

import com.example.swedemo.global.common.enums.Language;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

/**
 * Python 러너. 컴파일 단계가 없는 인터프리터 언어.
 */
@Component
public class PythonRunner extends AbstractProcessRunner {

    @Override
    public Language language() {
        return Language.PYTHON;
    }

    @Override
    public void prepare(Path dir, String source) {
        writeSource(dir, "Main.py", source);
    }

    @Override
    public ExecResult run(Path dir, String input, long timeoutMs) {
        return execute(dir, List.of("python3", "Main.py"), input, timeoutMs);
    }
}
