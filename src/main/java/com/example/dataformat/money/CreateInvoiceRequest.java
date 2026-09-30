package com.example.dataformat.money;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInvoiceRequest(@NotBlank(message = "customerId is required")
                                   String customerId,

                                   @NotNull(message = "total is required")
                                   @Valid
                                   Money total) {
}
