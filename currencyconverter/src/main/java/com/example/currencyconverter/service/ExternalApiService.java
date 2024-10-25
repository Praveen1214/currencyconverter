// ExternalApiService.java
package com.example.currencyconverter.service;

import com.example.currencyconverter.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExternalApiService {

    @Value("${currency.api.key}")
    private String apiKey;

    @Value("${currency.api.url.latest}")
    private String latestApiUrl;

    private final RestTemplate restTemplate;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "exchangeRates", key = "#toCurrency")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            // Free-tier accounts can't change the base currency, it's always EUR
            String url = String.format("%s?access_key=%s&symbols=%s",
                    latestApiUrl, apiKey, toCurrency);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println("API Response: " + response);

            if (response == null || !response.containsKey("rates")) {
                throw new ApiException("Invalid response from exchange rate API");
            }

            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            if (!rates.containsKey(toCurrency)) {
                throw new ApiException("Target currency not found in response");
            }

            return BigDecimal.valueOf(Double.parseDouble(rates.get(toCurrency).toString()));
        } catch (Exception e) {
            throw new ApiException("Error fetching exchange rate: " + e.getMessage());
        }
    }
}
