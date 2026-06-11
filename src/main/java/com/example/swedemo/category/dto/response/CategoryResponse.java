package com.example.swedemo.category.dto.response;

import com.example.swedemo.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카테고리 응답")
public class CategoryResponse {

    @Schema(description = "카테고리 ID")
    private Long categoryId;

    @Schema(description = "카테고리 이름")
    private String categoryName;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
