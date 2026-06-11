package com.example.swedemo.provider.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "제공자 생성 요청")
public class ProviderCreateRequest {

    @Schema(description = "제공자 이름 (예: GOOGLE, KAKAO, GITHUB)", example = "GOOGLE")
    private String provider;
}
