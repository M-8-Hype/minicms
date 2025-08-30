package com.suesskind.minicms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class BlogEntry {
    @Id @Column(columnDefinition = "BINARY(16)") private UUID id;
    private String title;
    private String content;
    private String author;
    private LocalDate releaseDate;
    @ManyToMany private Set<Category> categories;

    public BlogEntry() { }

    public BlogEntry(UUID id, String title, String content, String author, LocalDate releaseDate, Set<Category> categories) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.releaseDate = releaseDate;
        this.categories = categories;
    }

    public UUID getId() {
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
