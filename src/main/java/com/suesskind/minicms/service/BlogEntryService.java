package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.repository.BlogEntryRepository;
import com.suesskind.minicms.repository.CategoryRepository;
import com.suesskind.minicms.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogEntryService {

    @Autowired private BlogEntryRepository blogEntryRepository;
    @Autowired private CategoryRepository categoryRepository;

    public List<BlogEntry> getBlogEntries(List<String> categoryNames) {
        return categoryNames == null || categoryNames.isEmpty()
                ? blogEntryRepository.findAll()
                : blogEntryRepository.findDistinctByCategoriesNameIn(categoryNames);
    }

    public Optional<BlogEntry> getBlogEntryById(String id) {
        UUID uuid = UuidUtils.parseId(id);
        return blogEntryRepository.findById(uuid);
    }

    public BlogEntry createBlogEntry(BlogEntryRequestDto blogEntryRequestDto) {
        Set<Category> categories = blogEntryRequestDto.getCategoryIds().stream()
                .map(UuidUtils::parseId)
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        if (categories.isEmpty()) {
            return null;
        }
        BlogEntry blogEntry = mapToEntity(blogEntryRequestDto, categories);
        return blogEntryRepository.save(blogEntry);
    }

    public Optional<BlogEntry> updateBlogEntry(String id, BlogEntryRequestDto blogEntryRequestDto) {
        UUID uuid = UuidUtils.parseId(id);
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
        UUID uuid = UuidUtils.parseId(id);
        blogEntryRepository.deleteById(uuid);
    }

    private BlogEntry mapToEntity(BlogEntryRequestDto requestDto, Set<Category> categories) {
        return new BlogEntry(
                UuidUtils.generateId(),
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor(),
                getCurrentDate(),
                categories
        );
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
