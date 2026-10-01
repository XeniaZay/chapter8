package com.example.dataformat.negotiation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-card")
public class CustomerCardController {

    @GetMapping(value = "/{id}",
            produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })

    CustomerCardResponse getById(@PathVariable String id){
        return new CustomerCardResponse(id, "Alice", "alice@example.com");
    }

}
