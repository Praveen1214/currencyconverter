package com.example.currencyconverter.util;

import com.example.currencyconverter.dto.request.ConversionRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyValidator {
    public boolean isValidRequest(ConversionRequest request) {
        return request.getAmount().compareTo(BigDecimal.ZERO) > 0;
    }
}
