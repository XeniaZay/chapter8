package com.example.dataformat.domainerrors;

import com.example.dataformat.money.Money;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(String transferId,
                               String fromAccountId,
                               String toAccountId,
                               BigDecimal amount,
                               String status) {
}
