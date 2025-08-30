package com.suesskind.minicms.controller;

import com.suesskind.minicms.dto.BlogEntryRequestDto;
import com.suesskind.minicms.dto.BlogEntryResponseDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.model.Category;
import com.suesskind.minicms.service.BlogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blog")
public class BlogEntryController {

    @Autowired
    private BlogEntryService blogEntryService;

    @GetMapping
    public ResponseEntity<List<BlogEntryResponseDto>> getBlogEntries() {
        List<BlogEntry> entries = blogEntryService.getBlogEntries();
        List<BlogEntryResponseDto> dtos = entries.stream()
                .map(this::mapToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogEntryResponseDto> getBlogEntryById(@PathVariable String id) {
        return blogEntryService.getBlogEntryById(id)
                .map(entity -> {
                    BlogEntryResponseDto dto = mapToDto(entity);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BlogEntryResponseDto> createBlogEntry(@RequestBody BlogEntryRequestDto blogEntryRequestDto) {
        BlogEntry blogEntry = blogEntryService.createBlogEntry(blogEntryRequestDto);
        BlogEntryResponseDto dto = mapToDto(blogEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogEntryResponseDto> updateBlogEntry(@PathVariable String id, @RequestBody BlogEntryRequestDto blogEntryRequestDto) {
        return blogEntryService.updateBlogEntry(id, blogEntryRequestDto)
                .map(updatedEntry -> {
                    BlogEntryResponseDto dto = mapToDto(updatedEntry);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogEntry(@PathVariable String id) {
        return blogEntryService.getBlogEntryById(id)
                .map(entity -> {
                    blogEntryService.deleteBlogEntry(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private BlogEntryResponseDto mapToDto(BlogEntry blogEntry) {
        Set<String> categoryNames = blogEntry.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
        return new BlogEntryResponseDto(
                blogEntry.getId().toString(),
                blogEntry.getTitle(),
                blogEntry.getContent(),
                blogEntry.getAuthor(),
                blogEntry.getReleaseDate().toString(),
                categoryNames
        );
    }
}
