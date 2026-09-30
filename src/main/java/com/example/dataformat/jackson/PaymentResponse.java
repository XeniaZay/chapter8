package com.example.dataformat.jackson;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

public record PaymentResponse(String id,
                              BigDecimal amount,
                              Currency currency,
                              Instant createdAt,
                              String comment) {
}
