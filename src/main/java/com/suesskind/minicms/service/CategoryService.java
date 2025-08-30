package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.CategoryRequestDto;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.repository.CategoryRepository;
import com.suesskind.minicms.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<Category> getCategoryById(String id) {
        UUID uuid = UuidUtils.parseId(id);
        return categoryRepository.findById(uuid);
    }

    public Category createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = mapToEntity(categoryRequestDto);
        return categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        UUID uuid = UuidUtils.parseId(id);
        categoryRepository.deleteById(uuid);
    }

    private Category mapToEntity(CategoryRequestDto requestDto) {
        return new Category(
                UuidUtils.generateId(),
                requestDto.getName()
        );
    }
}
