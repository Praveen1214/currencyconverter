package com.example.currencyconverter.service;

import com.example.currencyconverter.dto.request.ConversionRequest;
import com.example.currencyconverter.dto.response.ConversionResponse;
import com.example.currencyconverter.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private ExternalApiService externalApiService;

    @InjectMocks
    private CurrencyService currencyService;

    private ConversionRequest validRequest;
    private static final BigDecimal SAMPLE_AMOUNT = BigDecimal.valueOf(100.00);
    private static final BigDecimal SAMPLE_RATE = BigDecimal.valueOf(0.85);

    @BeforeEach
    void setUp() {
        validRequest = new ConversionRequest();
        validRequest.setSourceCurrency("USD");
        validRequest.setTargetCurrency("EUR");
        validRequest.setAmount(SAMPLE_AMOUNT);
    }

    @Nested
    @DisplayName("Convert Currency Tests")
    class ConvertCurrencyTests {

        @Test
        @DisplayName("Should successfully convert currency with valid request")
        void shouldConvertCurrencySuccessfully() {
            // Arrange
            when(externalApiService.getExchangeRate("USD", "EUR"))
                    .thenReturn(SAMPLE_RATE);

            // Act
            ConversionResponse response = currencyService.convertCurrency(validRequest);

            // Assert
            assertAll(
                    "Verify all properties of the conversion response",
                    () -> assertEquals("USD", response.getSourceCurrency()),
                    () -> assertEquals("EUR", response.getTargetCurrency()),
                    () -> assertEquals(SAMPLE_AMOUNT, response.getSourceAmount()),
                    () -> assertEquals(
                            SAMPLE_AMOUNT.multiply(SAMPLE_RATE).setScale(2, RoundingMode.HALF_UP),
                            response.getConvertedAmount()
                    ),
                    () -> assertEquals(SAMPLE_RATE, response.getExchangeRate()),
                    () -> assertNotNull(response.getTimestamp())
            );

            // Verify external service was called exactly once
            verify(externalApiService, times(1))
                    .getExchangeRate("USD", "EUR");
        }

        @Test
        @DisplayName("Should throw exception when source currency is unsupported")
        void shouldThrowExceptionForUnsupportedSourceCurrency() {
            // Arrange
            validRequest.setSourceCurrency("XXX");

            // Act & Assert
            ApiException exception = assertThrows(
                    ApiException.class,
                    () -> currencyService.convertCurrency(validRequest)
            );
            assertEquals("Unsupported source currency: XXX", exception.getMessage());
            verify(externalApiService, never()).getExchangeRate(anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when target currency is unsupported")
        void shouldThrowExceptionForUnsupportedTargetCurrency() {
            // Arrange
            validRequest.setTargetCurrency("XXX");

            // Act & Assert
            ApiException exception = assertThrows(
                    ApiException.class,
                    () -> currencyService.convertCurrency(validRequest)
            );
            assertEquals("Unsupported target currency: XXX", exception.getMessage());
            verify(externalApiService, never()).getExchangeRate(anyString(), anyString());
        }

        @Test
        @DisplayName("Should handle external API failure")
        void shouldHandleExternalApiFailure() {
            // Arrange
            when(externalApiService.getExchangeRate("USD", "EUR"))
                    .thenThrow(new ApiException("External API error"));

            // Act & Assert
            ApiException exception = assertThrows(
                    ApiException.class,
                    () -> currencyService.convertCurrency(validRequest)
            );
            assertEquals("External API error", exception.getMessage());
        }

        @Test
        @DisplayName("Should round converted amount to 2 decimal places")
        void shouldRoundConvertedAmountCorrectly() {
            // Arrange
            BigDecimal irregularRate = BigDecimal.valueOf(0.8533333);
            when(externalApiService.getExchangeRate("USD", "EUR"))
                    .thenReturn(irregularRate);

            // Act
            ConversionResponse response = currencyService.convertCurrency(validRequest);

            // Assert
            assertEquals(
                    SAMPLE_AMOUNT.multiply(irregularRate).setScale(2, RoundingMode.HALF_UP),
                    response.getConvertedAmount()
            );
        }
    }

    @Nested
    @DisplayName("Supported Currencies Tests")
    class SupportedCurrenciesTests {

        @Test
        @DisplayName("Should return all supported currencies")
        void shouldReturnAllSupportedCurrencies() {
            // Act
            List<String> supportedCurrencies = currencyService.getSupportedCurrencies();

            // Assert
            assertAll(
                    "Verify supported currencies list",
                    () -> assertTrue(supportedCurrencies.contains("USD")),
                    () -> assertTrue(supportedCurrencies.contains("EUR")),
                    () -> assertTrue(supportedCurrencies.contains("GBP")),
                    () -> assertFalse(supportedCurrencies.contains("XXX")),
                    () -> assertTrue(supportedCurrencies.size() >= 3)
            );
        }

        @Test
        @DisplayName("Should return unmodifiable list of currencies")
        void shouldReturnUnmodifiableList() {
            // Act
            List<String> supportedCurrencies = currencyService.getSupportedCurrencies();

            // Assert
            assertThrows(
                    UnsupportedOperationException.class,
                    () -> supportedCurrencies.add("XXX")
            );
        }
    }
}