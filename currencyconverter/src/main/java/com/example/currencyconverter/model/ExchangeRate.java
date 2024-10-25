package com.example.currencyconverter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;

    // Getters and Setters
}
