package com.suesskind.minicms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.dto.BlogEntryResponseDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.model.BlogStatus;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.service.BlogEntryService;
import com.suesskind.minicms.util.UuidUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.suesskind.minicms.controller.BlogEntryController.mapToDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogEntryController.class)
public class BlogEntryControllerTest {

    private static final BlogEntry TEST_ENTRY = new BlogEntry(
            UuidUtils.generateId(),
            "Neues Java-Release",
            "Viele tolle Features …",
            "Max Mustermann",
            LocalDate.now(),
            Set.of(new Category(UuidUtils.generateId(), "Tech"), new Category(UuidUtils.generateId(), "Sport"))
    );
    private static final BlogEntryResponseDto EXPECTED_DTO = mapToDto(TEST_ENTRY);

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BlogEntryService blogEntryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetBlogEntries() throws Exception {
        Mockito.when(blogEntryService.getBlogEntries(null)).thenReturn(List.of(TEST_ENTRY));

        mockMvc.perform(get("/api/blog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(EXPECTED_DTO.getTitle()))
                .andExpect(jsonPath("$[0].content").value(EXPECTED_DTO.getContent()))
                .andExpect(jsonPath("$[0].categories").isArray())
                .andExpect(jsonPath("$[0].categories.length()").value(EXPECTED_DTO.getCategories().size()))
                .andExpect(jsonPath("$[0].categories[0]").value(EXPECTED_DTO.getCategories().toArray()[0]))
                .andExpect(jsonPath("$[0].status").value(EXPECTED_DTO.getStatus()));
    }

    @Test
    public void testGetBlogEntryById_found() throws Exception {
        Mockito.when(blogEntryService.getBlogEntryById(TEST_ENTRY.getId().toString())).thenReturn(Optional.of(TEST_ENTRY));

        mockMvc.perform(get("/api/blog/{id}", TEST_ENTRY.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXPECTED_DTO.getId()))
                .andExpect(jsonPath("$.author").value(EXPECTED_DTO.getAuthor()))
                .andExpect(jsonPath("$.categories[1]").value(EXPECTED_DTO.getCategories().toArray()[1]));
    }

    @Test
    public void testGetBlogEntryById_notFound() throws Exception {
        Mockito.when(blogEntryService.getBlogEntryById("Invalid ID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/blog/{id}", TEST_ENTRY.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBlogEntry_success() throws Exception {
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                TEST_ENTRY.getTitle(),
                TEST_ENTRY.getContent(),
                TEST_ENTRY.getAuthor(),
                Set.of(TEST_ENTRY.getCategories().stream().toList().get(0).getId().toString(),
                        TEST_ENTRY.getCategories().stream().toList().get(1).getId().toString()),
                BlogStatus.PUBLISHED.getDisplayName()
        );

        Mockito.when(blogEntryService.createBlogEntry(Mockito.any())).thenReturn(TEST_ENTRY);

        mockMvc.perform(post("/api/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(EXPECTED_DTO.getTitle()))
                .andExpect(jsonPath("$.status").value(EXPECTED_DTO.getStatus()));
    }

    @Test
    public void testCreateBlogEntry_badRequest() throws Exception {
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                TEST_ENTRY.getTitle(),
                TEST_ENTRY.getContent(),
                TEST_ENTRY.getAuthor(),
                Set.of(),
                BlogStatus.DRAFT.getDisplayName()
        );

        Mockito.when(blogEntryService.createBlogEntry(Mockito.any())).thenReturn(null);

        mockMvc.perform(post("/api/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateBlogEntry_success() throws Exception {
        String newContent = "Viele tolle Features und Verbesserungen …";
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                "",
                newContent,
                null,
                null,
                BlogStatus.PUBLISHED.getDisplayName()
        );

        BlogEntry updatedEntry = new BlogEntry(
                TEST_ENTRY.getId(),
                TEST_ENTRY.getTitle(),
                newContent,
                TEST_ENTRY.getAuthor(),
                TEST_ENTRY.getReleaseDate(),
                TEST_ENTRY.getCategories()
        );
        updatedEntry.setStatus(BlogStatus.PUBLISHED);

        Mockito.when(blogEntryService.updateBlogEntry(Mockito.eq(TEST_ENTRY.getId().toString()), Mockito.any()))
                .thenReturn(Optional.of(updatedEntry));

        mockMvc.perform(put("/api/blog/{id}", TEST_ENTRY.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(newContent))
                .andExpect(jsonPath("$.status").value(BlogStatus.PUBLISHED.getDisplayName()));
    }

    @Test
    public void testUpdateBlogEntry_notFound() throws Exception {
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                "Updated Title",
                null,
                "Updated Author",
                null,
                null
        );

        Mockito.when(blogEntryService.updateBlogEntry(Mockito.eq("Invalid ID"), Mockito.any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/blog/{id}", "Invalid ID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBlogEntry_success() throws Exception {
        Mockito.when(blogEntryService.getBlogEntryById(TEST_ENTRY.getId().toString()))
                .thenReturn(Optional.of(TEST_ENTRY));
        Mockito.doNothing().when(blogEntryService).deleteBlogEntry(TEST_ENTRY.getId().toString());

        mockMvc.perform(delete("/api/blog/{id}", TEST_ENTRY.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBlogEntry_notFound() throws Exception {
        Mockito.when(blogEntryService.getBlogEntryById("Invalid ID"))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/blog/{id}", "Invalid ID"))
                .andExpect(status().isNotFound());
    }
}
