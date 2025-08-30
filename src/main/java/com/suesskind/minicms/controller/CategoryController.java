package com.suesskind.minicms.controller;

import com.suesskind.minicms.dto.CategoryRequestDto;
import com.suesskind.minicms.dto.CategoryResponseDto;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        Category category = categoryService.createCategory(categoryRequestDto);
        CategoryResponseDto dto = mapToDto(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        return categoryService.getCategoryById(id)
                .map(entity -> {
                    categoryService.deleteCategory(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private CategoryResponseDto mapToDto(Category category) {
        return new CategoryResponseDto(
                category.getId().toString(),
                category.getName()
        );
    }
}
