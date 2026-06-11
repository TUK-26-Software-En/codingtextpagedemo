package com.example.swedemo.supervisor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "감독자 생성 요청")
public class SupervisorCreateRequest {

    @Schema(description = "제공자 ID", example = "1")
    private Long providerId;

    @Schema(description = "감독자 이름", example = "김교수")
    private String supervisorName;
}
