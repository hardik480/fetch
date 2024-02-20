package com.receipt.processor.service.controller;

import com.receipt.processor.service.entity.Receipt;
import com.receipt.processor.service.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<?> processReceipt(@RequestBody Receipt receipt) {
        String id = receiptService.processReceipt(receipt);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable String id) {
        int points = receiptService.getPoints(id);
        if (points == -1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receipt not found");
        else
            return ResponseEntity.ok().body(points);
    }
}

