package com.example.dataformat.strict;

import com.example.dataformat.jackson.PaymentController;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final AtomicLong idGenerator = new AtomicLong(0);

    @PostMapping
    ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        Long id = idGenerator.incrementAndGet();

        CustomerResponse response = new CustomerResponse(
                id,
                request.name(),
                request.email(),
                request.age()
        );
        log.info("Customer created: id={}, name={}", id,request.name());
        return ResponseEntity
                .created(URI.create("/api/customers/" + id))
                .body(response);
    }
}
