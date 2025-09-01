package com.suesskind.minicms.service;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.model.BlogStatus;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.repository.BlogEntryRepository;
import com.suesskind.minicms.repository.CategoryRepository;
import com.suesskind.minicms.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlogEntryService {

    @Autowired
    BlogEntryRepository blogEntryRepository;
    @Autowired
    CategoryRepository categoryRepository;

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
        return blogEntryRepository.findById(uuid)
                .map(entry -> {
                    updateEntryFromDto(entry, blogEntryRequestDto);
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

    private void updateEntryFromDto(BlogEntry blogEntry, BlogEntryRequestDto requestDto) {
        Optional.ofNullable(requestDto.getTitle())
                .filter(title -> !title.isBlank())
                .ifPresent(blogEntry::setTitle);
        Optional.ofNullable(requestDto.getContent())
                .filter(content -> !content.isBlank())
                .ifPresent(blogEntry::setContent);
        Optional.ofNullable(requestDto.getStatus())
                .filter(status -> !status.isBlank())
                .map(BlogStatus::fromJson)
                .ifPresent(blogEntry::setStatus);
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
