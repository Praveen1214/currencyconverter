package com.example.currencyconverter;

import com.example.currencyconverter.dto.request.ConversionRequest;
import com.example.currencyconverter.dto.response.ConversionResponse;
import com.example.currencyconverter.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyService currencyService;

    private ConversionRequest validRequest;
    private ConversionResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Set up valid request
        validRequest = new ConversionRequest();
        validRequest.setSourceCurrency("USD");
        validRequest.setTargetCurrency("EUR");
        validRequest.setAmount(BigDecimal.valueOf(100));

        // Set up mock response
        mockResponse = new ConversionResponse(
                "USD",
                "EUR",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(85.50),
                BigDecimal.valueOf(0.855)
        );
    }

    @Nested
    @DisplayName("Currency Conversion Tests")
    class CurrencyConversionTests {

        @Test
        @DisplayName("Should successfully convert currency with valid request")
        void shouldConvertCurrencySuccessfully() throws Exception {
            // Arrange
            when(currencyService.convertCurrency(any(ConversionRequest.class)))
                    .thenReturn(mockResponse);

            // Act & Assert
            mockMvc.perform(post("/api/currency/convert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.sourceCurrency", is("USD")))
                    .andExpect(jsonPath("$.targetCurrency", is("EUR")))
                    .andExpect(jsonPath("$.sourceAmount", is(100.0)))
                    .andExpect(jsonPath("$.convertedAmount", is(85.50)))
                    .andExpect(jsonPath("$.exchangeRate", is(0.855)))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        @DisplayName("Should return 400 when source currency is invalid")
        void shouldReturn400WhenSourceCurrencyIsInvalid() throws Exception {
            // Arrange
            validRequest.setSourceCurrency("INVALID");

            // Act & Assert
            performInvalidRequestTest(validRequest, "Unsupported source currency");
        }

        @Test
        @DisplayName("Should return 400 when target currency is invalid")
        void shouldReturn400WhenTargetCurrencyIsInvalid() throws Exception {
            // Arrange
            validRequest.setTargetCurrency("INVALID");

            // Act & Assert
            performInvalidRequestTest(validRequest, "Unsupported target currency");
        }

        @Test
        @DisplayName("Should return 400 when amount is negative")
        void shouldReturn400WhenAmountIsNegative() throws Exception {
            // Arrange
            validRequest.setAmount(BigDecimal.valueOf(-100));

            // Act & Assert
            performInvalidRequestTest(validRequest, "Amount must be positive");
        }

        @Test
        @DisplayName("Should return 400 when amount is zero")
        void shouldReturn400WhenAmountIsZero() throws Exception {
            // Arrange
            validRequest.setAmount(BigDecimal.ZERO);

            // Act & Assert
            performInvalidRequestTest(validRequest, "Amount must be positive");
        }
    }

    @Nested
    @DisplayName("Supported Currencies Tests")
    class SupportedCurrenciesTests {

        @Test
        @DisplayName("Should return list of supported currencies")
        void shouldReturnSupportedCurrencies() throws Exception {
            // Arrange
            List<String> supportedCurrencies = Arrays.asList("USD", "EUR", "GBP");
            when(currencyService.getSupportedCurrencies()).thenReturn(supportedCurrencies);

            // Act & Assert
            mockMvc.perform(get("/api/currency/supported-currencies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$", containsInAnyOrder("USD", "EUR", "GBP")));
        }
    }

    private void performInvalidRequestTest(ConversionRequest request, String expectedErrorMessage) throws Exception {
        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(expectedErrorMessage)));
    }
}