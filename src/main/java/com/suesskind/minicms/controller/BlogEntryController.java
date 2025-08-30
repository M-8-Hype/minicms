package com.suesskind.minicms.controller;

import com.suesskind.minicms.dto.BlogEntryResponseDto;
import com.suesskind.minicms.model.BlogEntry;
import com.suesskind.minicms.service.BlogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.suesskind.minicms.dto.BlogEntryRequestDto;

@RestController
@RequestMapping("/api/blog")
public class BlogEntryController {

    @Autowired
    private BlogEntryService blogEntryService;

    @GetMapping
    public String getBlogEntries() {
        return "List of blog entries";
    }

    @GetMapping("/{id}")
    public String getBlogEntryById(@PathVariable Long id) {
        return String.format("Blog entry with ID: %d", id);
    }

    @PostMapping
    public BlogEntryResponseDto createBlogEntry(@RequestBody BlogEntryRequestDto blogEntryRequestDto) {
        BlogEntry blogEntry = blogEntryService.createBlogEntry(blogEntryRequestDto);
        return mapToDto(blogEntry);
    }

    @PutMapping("/{id}")
    public String updateBlogEntry(@PathVariable Long id, @RequestBody String blogEntry) {
        return String.format("Updated blog entry with ID: %d to %s", id, blogEntry);
    }

    @DeleteMapping("/{id}")
    public String deleteBlogEntry(@PathVariable Long id) {
        return String.format("Deleted blog entry with ID: %d", id);
    }

    private BlogEntryResponseDto mapToDto(BlogEntry blogEntry) {
        return new BlogEntryResponseDto(
                blogEntry.getId().toString(),
                blogEntry.getTitle(),
                blogEntry.getContent(),
                blogEntry.getAuthor(),
                blogEntry.getReleaseDate()
        );
    }
}
