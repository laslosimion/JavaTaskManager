package com.example.barricade.service;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
