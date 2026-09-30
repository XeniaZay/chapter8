package com.example.dataformat.money;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class MoneyDeserializer extends ValueDeserializer<Money> {
    @Override
    public Money deserialize(JsonParser p,
                             DeserializationContext context) {
        String value = p.getValueAsString();
        if (value == null || value.isBlank()) {
            throw new JacksonException("money value is required") {};
        }
        try {
            return Money.parse(value);
        } catch (IllegalArgumentException ex) {
            throw new JacksonException(
                    "invalid money format '" + value + "': " + ex.getMessage()) {};
        }
    }
}
