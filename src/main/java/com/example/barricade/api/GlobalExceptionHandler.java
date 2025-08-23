package com.example.barricade.api;

import com.example.barricade.service.ConflictException;
import com.example.barricade.service.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorPayload> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ErrorPayload p = new ErrorPayload();
        p.status = HttpStatus.BAD_REQUEST.value();
        p.error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        p.message = "Validation failed";
        p.path = req.getRequestURI();
        p.fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorPayload.FieldError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(p, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorPayload> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        ErrorPayload p = new ErrorPayload();
        p.status = HttpStatus.NOT_FOUND.value();
        p.error = HttpStatus.NOT_FOUND.getReasonPhrase();
        p.message = ex.getMessage();
        p.path = req.getRequestURI();
        return new ResponseEntity<>(p, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorPayload> handleConflict(ConflictException ex, HttpServletRequest req) {
        ErrorPayload p = new ErrorPayload();
        p.status = HttpStatus.CONFLICT.value();
        p.error = HttpStatus.CONFLICT.getReasonPhrase();
        p.message = ex.getMessage();
        p.path = req.getRequestURI();
        return new ResponseEntity<>(p, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorPayload> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ErrorPayload p = new ErrorPayload();
        p.status = HttpStatus.BAD_REQUEST.value();
        p.error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        p.message = "Data integrity violation: " + ex.getMostSpecificCause().getMessage();
        p.path = req.getRequestURI();
        return new ResponseEntity<>(p, HttpStatus.BAD_REQUEST);
    }
}
