package com.example.swedemo.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 생성 요청")
public class UserCreateRequest {

    @Schema(description = "제공자 ID", example = "1")
    private Long providerId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "사용자 소개", example = "안녕하세요!")
    private String userInfo;
}
