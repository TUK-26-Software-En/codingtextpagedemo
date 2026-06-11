package com.example.swedemo.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 정보 수정 요청")
public class UserUpdateRequest {

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "사용자 소개")
    private String userInfo;
}
