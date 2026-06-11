package com.example.swedemo.provider.dto.response;

import com.example.swedemo.provider.entity.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "제공자 응답")
public class ProviderResponse {

    @Schema(description = "제공자 ID")
    private Long providerId;

    @Schema(description = "제공자 이름")
    private String provider;

    public static ProviderResponse from(Provider provider) {
        return ProviderResponse.builder()
                .providerId(provider.getProviderId())
                .provider(provider.getProvider())
                .build();
    }
}
