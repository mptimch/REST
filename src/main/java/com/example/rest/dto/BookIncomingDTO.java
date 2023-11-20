package com.example.rest.dto;

import java.util.List;

public class BookIncomingDTO {
    private int id;
    private String name;
    private int price;
    private int authorId;
    private List<Integer> genresId;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public List<Integer> getGenresId() {
        return genresId;
    }

    public void setGenresId(List<Integer> genresId) {
        this.genresId = genresId;
    }
}
