package com.example.swedemo.category.service;

import com.example.swedemo.category.dto.request.CategoryCreateRequest;
import com.example.swedemo.category.dto.response.CategoryResponse;
import com.example.swedemo.category.entity.Category;
import com.example.swedemo.category.repository.CategoryRepository;
import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .build();
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public CategoryResponse findById(Long id) {
        return CategoryResponse.from(getById(id));
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(getById(id));
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
