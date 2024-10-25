// ConversionRequest.java
package com.example.currencyconverter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ConversionRequest {
    @NotBlank(message = "Source currency is required")
    private String sourceCurrency;

    @NotBlank(message = "Target currency is required")
    private String targetCurrency;

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