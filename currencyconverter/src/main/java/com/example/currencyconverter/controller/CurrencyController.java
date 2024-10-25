package com.example.currencyconverter.controller;

import com.example.currencyconverter.dto.request.ConversionRequest;
import com.example.currencyconverter.dto.response.ConversionResponse;
import com.example.currencyconverter.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
        return ResponseEntity.ok(currencyService.convertCurrency(request));
    }

    @GetMapping("/supported-currencies")
    public ResponseEntity<List<String>> getSupportedCurrencies() {
        return ResponseEntity.ok(currencyService.getSupportedCurrencies());
    }
}