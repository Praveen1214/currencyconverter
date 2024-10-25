// ConversionRequest.java
package com.example.currencyconverter.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ConversionRequest {
    @NotBlank(message = "Source currency cannot be blank")
    private String sourceCurrency;

    @NotBlank(message = "Target currency cannot be blank")
    private String targetCurrency;

    @DecimalMin(value = "0.01", message = "Amount must be positive")
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