package com.example.swedemo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 응답. 발급된 JWT와 식별 정보.
 */
@Getter
@AllArgsConstructor
@Schema(description = "토큰 응답")
public class TokenResponse {

    @Schema(description = "JWT 액세스 토큰")
    private String accessToken;

    @Schema(description = "역할", example = "USER")
    private String role;

    @Schema(description = "식별자", example = "1")
    private Long subjectId;
}
