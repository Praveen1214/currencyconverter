package com.example.currencyconverter.service;

import com.example.currencyconverter.dto.request.ConversionRequest;
import com.example.currencyconverter.dto.response.ConversionResponse;
import com.example.currencyconverter.exception.ApiException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CurrencyService {

    private final ExternalApiService externalApiService;
    private final Set<String> supportedCurrencies = Set.of("USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "INR");

    public CurrencyService(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {
        validateCurrencies(request.getSourceCurrency(), request.getTargetCurrency());

        BigDecimal exchangeRate = externalApiService.getExchangeRate(
                request.getSourceCurrency(),
                request.getTargetCurrency()
        );

        BigDecimal convertedAmount = request.getAmount()
                .multiply(exchangeRate)
                .setScale(2, RoundingMode.HALF_UP);

        return new ConversionResponse(
                request.getSourceCurrency(),
                request.getTargetCurrency(),
                request.getAmount(),
                convertedAmount,
                exchangeRate
        );
    }

    private void validateCurrencies(String sourceCurrency, String targetCurrency) {
        if (!supportedCurrencies.contains(sourceCurrency)) {
            throw new ApiException("Unsupported source currency: " + sourceCurrency);
        }
        if (!supportedCurrencies.contains(targetCurrency)) {
            throw new ApiException("Unsupported target currency: " + targetCurrency);
        }
    }

    public List<String> getSupportedCurrencies() {
        return new ArrayList<>(supportedCurrencies);
    }
}
