package com.example.dataformat.domainerrors;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    TransferService service;

    public TransferController(TransferService service){
        this.service = service;
    }

    @PostMapping
    ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request){
        TransferResponse response = service.transfer(request);
        return ResponseEntity.ok(response);
    }
}
