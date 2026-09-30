package com.example.dataformat.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Money {
    private final long amountMinor;
    private final String currency;

    public Money(long amountMinor, String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency is required");
        }
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("invalid ISO currency: " + currency);
        }
        this.amountMinor = amountMinor;
        this.currency = currency.toUpperCase();
    }

    public long getAmountMinor() {
        return amountMinor;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal toMajorUnits() {
        return BigDecimal.valueOf(amountMinor)
                .movePointLeft(minorUnits())
                .setScale(minorUnits(), RoundingMode.UNNECESSARY);
    }

    public int minorUnits() {
        return Currency.getInstance(currency).getDefaultFractionDigits();
    }

    public static Money parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("money value is required");
        }

        String trimmed = value.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace < 1 || lastSpace == trimmed.length() - 1) {
            throw new IllegalArgumentException(
                    "money format must be '<amount> <currency>', got: " + value);
        }

        String amountPart = trimmed.substring(0, lastSpace).trim();
        String currencyPart = trimmed.substring(lastSpace + 1).trim();

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountPart);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "invalid amount: " + amountPart, ex);
        }

        Currency currency;
        try {
            currency = Currency.getInstance(currencyPart);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "invalid currency: " + currencyPart, ex);
        }

        int scale = currency.getDefaultFractionDigits();
        if (amount.scale() > scale) {
            throw new IllegalArgumentException(
                    "amount has too many decimal places for " + currencyPart +
                            ": expected scale " + scale + ", got " + amount.scale());
        }

        long amountMinor = amount.movePointRight(scale).longValueExact();
        return new Money(amountMinor, currencyPart);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amountMinor == money.amountMinor
                && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountMinor, currency);
    }

    @Override
    public String toString() {
        return toMajorUnits().toPlainString() + " " + currency;
    }
}
