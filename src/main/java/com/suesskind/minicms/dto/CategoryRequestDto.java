package com.suesskind.minicms.dto;

public class CategoryRequestDto {
    private String name;

    public CategoryRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
