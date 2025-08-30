package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.repository.BlogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlogEntryService {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    public List<BlogEntry> getBlogEntries() {
        return blogEntryRepository.findAll();
    }

    public Optional<BlogEntry> getBlogEntryById(String id) {
        UUID uuid = parseId(id);
        return blogEntryRepository.findById(uuid);
    }

    public BlogEntry createBlogEntry(BlogEntryRequestDto blogEntryRequestDto) {
        BlogEntry blogEntry = mapToEntity(blogEntryRequestDto);
        return blogEntryRepository.save(blogEntry);
    }

    public Optional<BlogEntry> updateBlogEntry(String id, BlogEntryRequestDto blogEntryRequestDto) {
        UUID uuid = parseId(id);
        String newTitle = blogEntryRequestDto.getTitle();
        String newContent = blogEntryRequestDto.getContent();
        return blogEntryRepository.findById(uuid)
                .map(entry -> {
                    if (newTitle != null && !newTitle.isBlank()) {
                        entry.setTitle(newTitle);
                    }
                    if (newContent != null && !newContent.isBlank()) {
                        entry.setContent(newContent);
                    }
                    return blogEntryRepository.save(entry);
                });
    }

    public void deleteBlogEntry(String id) {
        UUID uuid = parseId(id);
        blogEntryRepository.deleteById(uuid);
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

    private UUID parseId(String id) {
        return UUID.fromString(id);
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
