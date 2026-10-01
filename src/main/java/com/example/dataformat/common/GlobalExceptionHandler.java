package com.example.dataformat.common;

import com.example.dataformat.domainerrors.AccountClosedException;
import com.example.dataformat.domainerrors.InsufficientFundsException;
import com.example.dataformat.problem.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
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

    @ExceptionHandler(AccountClosedException.class)
    ProblemDetail validation(AccountClosedException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Account is closed");
        problem.setDetail(ex.getMessage());
        problem.setProperty("code", "ACC_CLOSED");
        return problem;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    ProblemDetail validation(InsufficientFundsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problem.setTitle("Insufficient funds");
        problem.setDetail(ex.getMessage());
        problem.setProperty("code", "PAY_INSUFFICIENT_FUNDS");
        return problem;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    ProblemDetail handleNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                      HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_ACCEPTABLE,
                "Requested media type is not supported"
        );
        problem.setTitle("Not Acceptable");
        problem.setType(URI.create("https://api.example.com/problems/not-acceptable"));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", "NOT_ACCEPTABLE");
        problem.setProperty("supported", List.of("application/json", "application/xml"));
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }
}
