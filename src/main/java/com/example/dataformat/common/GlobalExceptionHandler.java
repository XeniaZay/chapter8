package com.example.dataformat.common;

import com.example.dataformat.problem.UserNotFoundException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail invalidJson(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid JSON");
        problem.setDetail(ex.getMostSpecificCause().getMessage());
        problem.setProperty("code", "JSON_INVALID");
        return problem;
    }

    @ExceptionHandler(UserNotFoundException.class)
    ProblemDetail userNotFound(UserNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setType(URI.create("https://example.com/problems/user-not-found"));
        problem.setTitle("User not found");
        problem.setDetail(ex.getMessage());
        problem.setProperty("code", "USER_NOT_FOUND");
        problem.setProperty("timestamp", Instant.now().toString());
        problem.setProperty("requestId", MDC.get("requestId"));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problem.setTitle("Validation failed");
        problem.setDetail("Request contains invalid fields");
        var violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "code", error.getCode(),
                        "message", error.getDefaultMessage()
                ))
                .toList();

        problem.setProperty("violations", violations);
        return problem;
    }
}
