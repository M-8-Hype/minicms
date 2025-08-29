package com.suesskind.minicms.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogEntryController {

    @GetMapping
    public String getBlogEntries() {
        return "List of blog entries";
    }

    @GetMapping("/{id}")
    public String getBlogEntryById(@PathVariable Long id) {
        return String.format("Blog entry with ID: %d", id);
    }

    @PostMapping
    public String createBlogEntry(@RequestBody String blogEntry) {
        return "Created blog entry: " + blogEntry;
    }

    @PutMapping("/{id}")
    public String updateBlogEntry(@PathVariable Long id, @RequestBody String blogEntry) {
        return String.format("Updated blog entry with ID: %d to %s", id, blogEntry);
    }

    @DeleteMapping("/{id}")
    public String deleteBlogEntry(@PathVariable Long id) {
        return String.format("Deleted blog entry with ID: %d", id);
    }
}
