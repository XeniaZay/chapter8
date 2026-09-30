package com.example.dataformat.jackson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping("/{id}")
    PaymentResponse getById(@PathVariable String id){
        PaymentResponse payment = new PaymentResponse(id,
                new BigDecimal("100.50"),
                Currency.getInstance("RUB"),
                Instant.parse("2026-09-30T12:00:00Z"),
                null);
        log.info("PaymentId={}, amount={}, currency={}, createdAt={}, comment={}", id,payment.amount(),payment.currency(),payment.createdAt(),payment.comment());
        return payment;

    }


}
