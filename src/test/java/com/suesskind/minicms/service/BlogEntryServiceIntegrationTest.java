package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.model.BlogStatus;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.repository.BlogEntryRepository;
import com.suesskind.minicms.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BlogEntryServiceIntegrationTest {

    @Autowired private BlogEntryRepository blogEntryRepository;
    @Autowired private CategoryRepository categoryRepository;

    private BlogEntryService blogEntryService;

    @BeforeEach
    public void setUp() {
        blogEntryService = new BlogEntryService();
        blogEntryService.blogEntryRepository = blogEntryRepository;
        blogEntryService.categoryRepository = categoryRepository;
    }

    @Test
    public void createBlogEntry_shouldPersistEntry_whenCategoriesValid() {
        UUID categoryId = UUID.randomUUID();
        Category category = categoryRepository.save(new Category(categoryId, "Tech"));
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                "Neues Java-Release",
                "Viele tolle Features …",
                "Max Mustermann",
                Set.of(category.getId().toString()),
                null
        );

        BlogEntry result = blogEntryService.createBlogEntry(requestDto);

        assertNotNull(result.getId());
        assertEquals("Neues Java-Release", result.getTitle());
        assertEquals(1, result.getCategories().size());
        assertEquals(BlogStatus.DRAFT, result.getStatus());
        assertTrue(blogEntryRepository.existsById(result.getId()));
    }
}
