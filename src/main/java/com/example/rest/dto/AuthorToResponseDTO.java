package com.example.rest.dto;

import java.util.List;

public class AuthorToResponseDTO {

    private String name;
    private List<String> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}
