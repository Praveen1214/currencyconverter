// ConversionRequest.java
package com.example.currencyconverter.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ConversionRequest {
    @NotNull(message = "Source currency is required")
    @Size(min = 3, max = 3, message = "Source currency must be a 3-letter code")
    private String sourceCurrency;

    @NotNull(message = "Target currency is required")
    @Size(min = 3, max = 3, message = "Target currency must be a 3-letter code")
    private String targetCurrency;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    // Getters and setters
    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency.toUpperCase();
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency.toUpperCase();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}