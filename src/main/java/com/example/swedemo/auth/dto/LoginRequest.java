package com.example.swedemo.auth.dto;

import com.example.swedemo.auth.AuthRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 개발용 로그인 요청. 역할과 기존 식별자(userId/supervisorId)로 토큰을 발급받는다.
 */
@Getter
@NoArgsConstructor
@Schema(description = "로그인 요청 (개발용)")
public class LoginRequest {

    @Schema(description = "역할", example = "USER")
    private AuthRole role;

    @Schema(description = "식별자 (userId 또는 supervisorId)", example = "1")
    private Long id;
}
