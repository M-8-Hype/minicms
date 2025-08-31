package com.suesskind.minicms.dto;

import java.util.Set;

public class BlogEntryRequestDto {
    private String title;
    private String content;
    private String author;
    private Set<String> categoryIds;
    private String status;

    public BlogEntryRequestDto(String title, String content, String author, Set<String> categoryIds, String status) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.categoryIds = categoryIds;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public Set<String> getCategoryIds() {
        return categoryIds;
    }

    public String getStatus() {
        return status;
    }
}
