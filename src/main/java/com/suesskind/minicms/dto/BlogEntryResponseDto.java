package com.suesskind.minicms.dto;

public class BlogEntryResponseDto {
    private String id;
    private String title;
    private String content;
    private String author;
    private String releaseDate;

    public BlogEntryResponseDto(String id, String title, String content, String author, String releaseDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.releaseDate = releaseDate;
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
}
