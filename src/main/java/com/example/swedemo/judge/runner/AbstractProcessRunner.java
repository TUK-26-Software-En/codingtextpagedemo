package com.example.swedemo.judge.runner;

import com.example.swedemo.judge.CompileException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 프로세스 실행 공통 로직(Template Method). 각 언어 러너는 소스 작성/명령 구성만 담당하고,
 * 실제 프로세스 기동·입출력 리다이렉트·타임아웃 처리는 본 클래스가 제공한다.
 * 파이프 데드락을 피하기 위해 stdin/stdout을 파일로 리다이렉트한다.
 */
public abstract class AbstractProcessRunner implements LanguageRunner {

    private static final long COMPILE_TIMEOUT_SECONDS = 20;

    /** 소스 파일 작성 */
    protected void writeSource(Path dir, String fileName, String source) {
        try {
            Files.writeString(dir.resolve(fileName),
                    source == null ? "" : source, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** 컴파일 명령 실행. 종료 코드가 0이 아니거나 타임아웃이면 CompileException. */
    protected void compile(Path dir, List<String> command) throws CompileException {
        Path logFile = dir.resolve("compile.log");
        try {
            Process process = new ProcessBuilder(command)
                    .directory(dir.toFile())
                    .redirectOutput(logFile.toFile())
                    .redirectErrorStream(true)
                    .start();

            boolean finished = process.waitFor(COMPILE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new CompileException("compile timeout");
            }
            if (process.exitValue() != 0) {
                throw new CompileException(readQuietly(logFile));
            }
        } catch (IOException e) {
            throw new CompileException(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CompileException("compile interrupted");
        }
    }

    /** 명령을 입력(stdin)과 함께 실행하고 결과를 ExecResult로 반환. */
    protected ExecResult execute(Path dir, List<String> command, String input, long timeoutMs) {
        Path inFile = dir.resolve("input.txt");
        Path outFile = dir.resolve("out.txt");
        try {
            Files.writeString(inFile, input == null ? "" : input, StandardCharsets.UTF_8);

            Process process = new ProcessBuilder(command)
                    .directory(dir.toFile())
                    .redirectInput(inFile.toFile())
                    .redirectOutput(outFile.toFile())
                    .redirectErrorStream(true)
                    .start();

            boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroyForcibly();
                process.waitFor();
                return ExecResult.builder().timedOut(true).exitCode(-1).stdout("").build();
            }
            return ExecResult.builder()
                    .timedOut(false)
                    .exitCode(process.exitValue())
                    .stdout(readQuietly(outFile))
                    .build();
        } catch (IOException e) {
            return ExecResult.builder().timedOut(false).exitCode(-1).stdout("").build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExecResult.builder().timedOut(false).exitCode(-1).stdout("").build();
        }
    }

    private String readQuietly(Path file) {
        try {
            return Files.exists(file) ? Files.readString(file, StandardCharsets.UTF_8) : "";
        } catch (IOException e) {
            return "";
        }
    }
}
