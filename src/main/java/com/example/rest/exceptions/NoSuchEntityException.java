package com.example.rest.exceptions;

public class NoSuchEntityException extends Exception {

    private int id;

    public NoSuchEntityException(int id, String message) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
