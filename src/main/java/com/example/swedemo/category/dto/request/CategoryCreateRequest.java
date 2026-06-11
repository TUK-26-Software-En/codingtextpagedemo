package com.example.swedemo.category.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "카테고리 생성 요청")
public class CategoryCreateRequest {

    @Schema(description = "카테고리 이름", example = "동적 프로그래밍")
    private String categoryName;
}
