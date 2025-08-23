package com.example.barricade.api;

import java.time.Instant;
import java.util.List;

public class ErrorPayload {
    public Instant timestamp = Instant.now();
    public int status;
    public String error;
    public String message;
    public String path;
    public List<FieldError> fieldErrors;

    public static class FieldError {
        public String field;
        public String message;
        public FieldError(String field, String message) {
            this.field = field; this.message = message;
        }
    }
}
