package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.repository.BlogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.suesskind.minicms.model.BlogEntry;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BlogEntryService {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    public BlogEntry createBlogEntry(BlogEntryRequestDto blogEntryRequestDto) {
        BlogEntry blogEntry = mapToEntity(blogEntryRequestDto);
        return blogEntryRepository.save(blogEntry);
    }

    private BlogEntry mapToEntity(BlogEntryRequestDto requestDto) {
        return new BlogEntry(
                generateId(),
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor(),
                getCurrentDate()
        );
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
