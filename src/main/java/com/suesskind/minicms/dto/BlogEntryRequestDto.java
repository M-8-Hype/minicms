package com.suesskind.minicms.dto;

public class BlogEntryRequestDto {
    private String title;
    private String content;
    private String author;
    private String releaseDate;

    public BlogEntryRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
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
}
