package com.example.dataformat.domainerrors;

import com.example.dataformat.money.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(@NotBlank(message = "fromAccountId is required")
                              String fromAccountId,

                              @NotBlank(message = "toAccountId is required")
                              String toAccountId,

                              @NotNull(message = "amount is required")
                              @Positive(message = "amount must be greater than zero")
                              BigDecimal amount) {
}
