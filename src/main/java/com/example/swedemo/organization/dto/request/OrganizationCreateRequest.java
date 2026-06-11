package com.example.swedemo.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "기관 생성 요청")
public class OrganizationCreateRequest {

    @Schema(description = "기관 이름", example = "서울대학교")
    private String organizationName;

    @Schema(description = "기관 설명")
    private String organizationDescription;
}
