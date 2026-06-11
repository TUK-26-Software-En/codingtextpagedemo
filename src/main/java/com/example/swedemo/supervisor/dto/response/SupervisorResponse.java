package com.example.swedemo.supervisor.dto.response;

import com.example.swedemo.supervisor.entity.Supervisor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "감독자 응답")
public class SupervisorResponse {

    @Schema(description = "감독자 ID")
    private Long supervisorId;

    @Schema(description = "제공자 이름")
    private String providerName;

    @Schema(description = "감독자 이름")
    private String supervisorName;

    public static SupervisorResponse from(Supervisor supervisor) {
        return SupervisorResponse.builder()
                .supervisorId(supervisor.getSupervisorId())
                .providerName(supervisor.getProvider().getProvider())
                .supervisorName(supervisor.getSupervisorName())
                .build();
    }
}
