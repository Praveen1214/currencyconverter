package com.example.currencyconverter.service;

import com.example.currencyconverter.dto.request.ConversionRequest;
import com.example.currencyconverter.dto.response.ConversionResponse;
import com.example.currencyconverter.exception.ApiException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
public class CurrencyService {

    private final ExternalApiService externalApiService;

    public CurrencyService(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    public List<String> getSupportedCurrencies() {
        // Get the supported currencies dynamically from the ExternalApiService
        Set<String> supportedCurrencies = externalApiService.getSupportedCurrencies();
        return List.copyOf(supportedCurrencies); // Convert Set to List
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {
        // Validate that the requested currencies are supported
        validateCurrencies(request.getSourceCurrency(), request.getTargetCurrency());

        // Validate that the amount is valid (greater than zero)
        validateAmount(request.getAmount());

        // Fetch the exchange rate from ExternalApiService
        BigDecimal exchangeRate = externalApiService.getExchangeRate(
                request.getSourceCurrency(),
                request.getTargetCurrency()
        );

        // Calculate the converted amount, rounded to 2 decimal places
        BigDecimal convertedAmount = request.getAmount()
                .multiply(exchangeRate)
                .setScale(2, RoundingMode.HALF_UP);

        // Return the ConversionResponse object
        return new ConversionResponse(
                request.getSourceCurrency(),
                request.getTargetCurrency(),
                request.getAmount(),
                convertedAmount,
                exchangeRate
        );
    }

    private void validateCurrencies(String sourceCurrency, String targetCurrency) {
        // Get supported currencies dynamically from the external API
        Set<String> supportedCurrencies = externalApiService.getSupportedCurrencies();

        // Check if source and target currencies are supported
        if (!supportedCurrencies.contains(sourceCurrency)) {
            throw new ApiException("Unsupported source currency: " + sourceCurrency);
        }
        if (!supportedCurrencies.contains(targetCurrency)) {
            throw new ApiException("Unsupported target currency: " + targetCurrency);
        }
    }

    private void validateAmount(BigDecimal amount) {
        // Ensure the amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Amount must be positive");
        }
    }
}
