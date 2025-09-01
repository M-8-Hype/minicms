package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.model.BlogStatus;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.repository.BlogEntryRepository;
import com.suesskind.minicms.repository.CategoryRepository;
import com.suesskind.minicms.util.UuidUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlogEntryServiceUnitTest {

    private static final BlogEntry TEST_ENTRY = new BlogEntry(
            UuidUtils.generateId(),
            "Neues Java-Release",
            "Viele tolle Features …",
            "Max Mustermann",
            LocalDate.now(),
            Set.of(new Category(UuidUtils.generateId(), "Tech"), new Category(UuidUtils.generateId(), "Sport"))
    );

    @Mock private BlogEntryRepository blogEntryRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private BlogEntryService blogEntryService;

    @Test
    public void createBlogEntry_shouldReturnSavedEntry_whenCategoriesValid() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category(categoryId, "Tech");
        BlogEntryRequestDto requestDto = new BlogEntryRequestDto(
                "Neues Java-Release",
                "Viele tolle Features …",
                "Max Mustermann",
                Set.of(categoryId.toString()),
                null
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(blogEntryRepository.save(any())).thenReturn(TEST_ENTRY);

        BlogEntry result = blogEntryService.createBlogEntry(requestDto);

        assertNotNull(result);
        assertEquals(BlogStatus.DRAFT, result.getStatus());
        verify(blogEntryRepository).save(any(BlogEntry.class));
    }
}
