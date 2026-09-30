package com.example.dataformat.money;

import com.example.dataformat.strict.CreateCustomerRequest;
import com.example.dataformat.strict.CustomerController;
import com.example.dataformat.strict.CustomerResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);


    @PostMapping
    ResponseEntity<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest request) {
        String id = UUID.randomUUID().toString();
        InvoiceResponse response = new InvoiceResponse(
                id,
                request.customerId(),
                request.total()
        );
        log.info("Customer created: id={}, customerId={}, total={}", id,request.customerId(),request.total());
        return ResponseEntity
                .created(URI.create("/api/invoices/" + id))
                .body(response);
    }
}
