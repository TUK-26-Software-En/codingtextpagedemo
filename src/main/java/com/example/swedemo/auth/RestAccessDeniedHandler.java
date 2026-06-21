package com.example.swedemo.auth;

import com.example.swedemo.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되었으나 권한이 없는 접근(403)을 표준 ApiResponse 형식 JSON으로 응답한다.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ErrorJsonWriter.write(response, HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN);
    }
}
