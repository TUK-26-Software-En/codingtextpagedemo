package com.example.swedemo.user.dto.response;

import com.example.swedemo.global.common.enums.Rank;
import com.example.swedemo.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 응답")
public class UserResponse {

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "제공자 ID")
    private Long providerId;

    @Schema(description = "제공자 이름")
    private String providerName;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "사용자 소개")
    private String userInfo;

    @Schema(description = "포인트")
    private int userPoint;

    @Schema(description = "랭크")
    private Rank userRank;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .providerId(user.getProvider().getProviderId())
                .providerName(user.getProvider().getProvider())
                .userName(user.getUserName())
                .userInfo(user.getUserInfo())
                .userPoint(user.getUserPoint())
                .userRank(user.getUserRank())
                .build();
    }
}
