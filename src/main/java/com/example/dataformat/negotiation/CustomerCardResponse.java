package com.example.dataformat.negotiation;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "customer")
public record CustomerCardResponse(String id,
                                   String name,
                                   String email) {
}
