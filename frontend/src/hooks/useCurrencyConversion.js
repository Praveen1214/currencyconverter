import { useState } from 'react';

export const useCurrencyConversion = () => {
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [lastUpdated, setLastUpdated] = useState(new Date());

  const convertCurrency = async ({ sourceCurrency, targetCurrency, amount }) => {
    if (parseFloat(amount) <= 0) {
      setError('Please enter a positive amount');
      return;
    }

    setLoading(true);
    setError('');

    try {
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

      if (!response.ok) throw new Error('Conversion failed');
      
      const data = await response.json();
      setResult(data);
      setLastUpdated(new Date());
      return data;
      
    } catch (err) {
      setError('Failed to convert currencies. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return {
    result,
    error,
    loading,
    lastUpdated,
    convertCurrency,
  };
};