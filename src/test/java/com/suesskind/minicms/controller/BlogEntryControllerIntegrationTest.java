package com.suesskind.minicms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.model.BlogStatus;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.repository.BlogEntryRepository;
import com.suesskind.minicms.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogEntryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BlogEntryRepository blogEntryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Category savedCategory;

    @BeforeEach
    void setUp() {
        blogEntryRepository.deleteAll();
        categoryRepository.deleteAll();
        UUID categoryId = UUID.randomUUID();
        savedCategory = categoryRepository.save(new Category(categoryId, "Tech"));
    }

    @Test
    public void testCreateBlogEntry() throws Exception {
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                "Neues Java-Release",
                "Viele tolle Features …",
                "Max Mustermann",
                Set.of(savedCategory.getId().toString()),
                null
        );

        mockMvc.perform(post("/api/blog")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Neues Java-Release"))
                .andExpect(jsonPath("$.content").value("Viele tolle Features …"))
                .andExpect(jsonPath("$.author").value("Max Mustermann"))
                .andExpect(jsonPath("$.categories[0]").value("Tech"))
                .andExpect(jsonPath("$.status").value(BlogStatus.DRAFT.getDisplayName()));
    }
}
