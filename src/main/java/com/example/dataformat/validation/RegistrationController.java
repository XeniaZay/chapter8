package com.example.dataformat.validation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final AtomicLong idGenerator = new AtomicLong(0);

    @PostMapping()
    ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        Long id = idGenerator.incrementAndGet();
        UserResponse response = new UserResponse(
                id, request.name(), request.email()
        );
        return ResponseEntity
                .created(URI.create("/api/users/" + id))
                .body(response);
    }
}
