// hooks/useHistoricalRates.js
import { useState, useEffect } from 'react';

export const useHistoricalRates = (sourceCurrency, targetCurrency, timeRange) => {
  const [historicalData, setHistoricalData] = useState([]);

  useEffect(() => {
    const generateHistoricalData = () => {
      const dataPoints = timeRange === 'week' ? 7 : timeRange === 'month' ? 30 : 90;
      const baseRate = 1.2;
      return Array.from({ length: dataPoints }, (_, i) => ({
        date: new Date(Date.now() - (dataPoints - 1 - i) * 24 * 60 * 60 * 1000).toLocaleDateString(),
        rate: (baseRate + Math.random() * 0.1).toFixed(4)
      }));
    };
    setHistoricalData(generateHistoricalData());
  }, [sourceCurrency, targetCurrency, timeRange]);

  return historicalData;
};