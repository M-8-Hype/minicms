package com.suesskind.minicms.dto;

public class CategoryResponseDto {
    private String id;
    private String name;

    public CategoryResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
