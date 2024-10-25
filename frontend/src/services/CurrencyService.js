// src/services/CurrencyService.js
export const convertCurrency = async (sourceCurrency, targetCurrency, amount) => {
    const response = await fetch('http://localhost:8080/api/currency/convert', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        sourceCurrency,
        targetCurrency,
        amount: parseFloat(amount)
      })
    });
  
    if (!response.ok) {
      throw new Error('Conversion failed');
    }
  
    return response.json();
  };
  