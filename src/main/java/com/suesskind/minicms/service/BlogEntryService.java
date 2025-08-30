package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import org.springframework.stereotype.Service;
import com.suesskind.minicms.model.BlogEntry;
import java.util.UUID;

@Service
public class BlogEntryService {

    public BlogEntry createBlogEntry(BlogEntryRequestDto blogEntryRequestDto) {
        return mapToEntity(blogEntryRequestDto);
    }

    private BlogEntry mapToEntity(BlogEntryRequestDto requestDto) {
        return new BlogEntry(
                generateId(),
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor(),
                requestDto.getReleaseDate()
        );
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }
}
