package com.suesskind.minicms.dto;

import java.util.Set;

public class BlogEntryResponseDto {
    private String id;
    private String title;
    private String content;
    private String author;
    private String releaseDate;
    private Set<String> categories;
    private String status;


    public BlogEntryResponseDto(String id, String title, String content, String author, String releaseDate, Set<String> categories, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.releaseDate = releaseDate;
        this.categories = categories;
        this.status = status;
    }

    public String getId() {
        return id;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public String getStatus() {
        return status;
    }
}
