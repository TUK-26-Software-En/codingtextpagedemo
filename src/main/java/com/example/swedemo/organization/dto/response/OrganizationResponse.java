package com.example.swedemo.organization.dto.response;

import com.example.swedemo.organization.entity.Organization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "기관 응답")
public class OrganizationResponse {

    @Schema(description = "기관 ID")
    private Long organizationId;

    @Schema(description = "기관 이름")
    private String organizationName;

    @Schema(description = "기관 설명")
    private String organizationDescription;

    public static OrganizationResponse from(Organization organization) {
        return OrganizationResponse.builder()
                .organizationId(organization.getOrganizationId())
                .organizationName(organization.getOrganizationName())
                .organizationDescription(organization.getOrganizationDescription())
                .build();
    }
}
