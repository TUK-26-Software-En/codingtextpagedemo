package com.example.swedemo.auth;

import com.example.swedemo.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 보안 필터 단계(컨트롤러 이전)에서 발생하는 인증/인가 예외를 표준 응답 형식으로 직접 기록한다.
 * 이 시점에는 메시지 컨버터가 동작하지 않으므로 ApiResponse 형식의 JSON을 수동 작성한다.
 */
final class ErrorJsonWriter {

    private ErrorJsonWriter() {
    }

    static void write(HttpServletResponse response, HttpStatus status, ErrorCode errorCode) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json = "{\"success\":false,\"code\":\"" + errorCode.name()
                + "\",\"message\":\"" + errorCode.getMessage()
                + "\",\"data\":null}";
        response.getWriter().write(json);
    }
}
