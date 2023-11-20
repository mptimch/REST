package com.example.rest.dto;

import java.util.List;

public class GenreIncomingDTO {

    private int id;
    private String name;
    private List<Integer> booksId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getBooksId() {
        return booksId;
    }

    public void setBooksId(List<Integer> booksId) {
        this.booksId = booksId;
    }
}
