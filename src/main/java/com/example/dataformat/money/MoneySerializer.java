package com.example.dataformat.money;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.io.IOException;

public class MoneySerializer extends ValueSerializer<Money> {

    @Override
    public void serialize(Money value,
                          JsonGenerator gen,
                          SerializationContext context) {
        gen.writeString(value.toString());
    }
}
