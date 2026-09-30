package com.example.dataformat.money;

public record InvoiceResponse(String id, String customerId, Money total) {
}
