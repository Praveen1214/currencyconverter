// CurrencyConverter.js (Main Component)
import React, { useState } from 'react';
import { Card, Button, Alert, Tooltip, Space, Typography } from 'antd';
import { SwapOutlined, SyncOutlined, DollarOutlined } from '@ant-design/icons';
import { CurrencyInput } from './components/CurrencyInput';
import { ConversionResult } from './components/ConversionResult';
import { HistoricalChart } from './components/HistoricalChart';
import { RecentSearches } from './components/RecentSearches';
import { useCurrencyConversion } from './hooks/useCurrencyConversion';
import { useHistoricalRates } from './hooks/useHistoricalRates';
import { useRecentSearches } from './hooks/useRecentSearches';
import { getCurrencySymbol } from './utils/currencyUtils';

const { Title, Text } = Typography;

const CurrencyConverter = () => {
  const [sourceCurrency, setSourceCurrency] = useState('USD');
  const [targetCurrency, setTargetCurrency] = useState('EUR');
  const [amount, setAmount] = useState('1');
  const [timeRange, setTimeRange] = useState('week');

  const { 
    result, 
    error, 
    loading, 
    lastUpdated, 
    convertCurrency 
  } = useCurrencyConversion();

  const historicalData = useHistoricalRates(sourceCurrency, targetCurrency, timeRange);
  const { recentSearches, addRecentSearch } = useRecentSearches();

  const handleConvert = async () => {
    const conversionResult = await convertCurrency({
      sourceCurrency,
      targetCurrency,
      amount
    });

    if (conversionResult) {
      addRecentSearch({
        from: sourceCurrency,
        to: targetCurrency,
        amount,
        result: conversionResult.convertedAmount,
        timestamp: new Date()
      });
    }
  };

  const swapCurrencies = () => {
    setSourceCurrency(targetCurrency);
    setTargetCurrency(sourceCurrency);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleConvert();
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <Card 
        title={
          <div className="text-center">
            <Title level={2} className="mb-2">Currency Converter</Title>
            <Text type="secondary">Get real-time exchange rates and currency conversion</Text>
          </div>
        }
        className="max-w-4xl mx-auto"
      >
        <Space direction="vertical" size="large" className="w-full">
          <CurrencyInput
            amount={amount}
            sourceCurrency={sourceCurrency}
            targetCurrency={targetCurrency}
            onAmountChange={setAmount}
            onSourceChange={setSourceCurrency}
            onTargetChange={setTargetCurrency}
            onKeyPress={handleKeyPress}
            getCurrencySymbol={getCurrencySymbol}
          />

          <div className="flex justify-center gap-4">
            <Tooltip title="Swap currencies">
              <Button
                icon={<SwapOutlined />}
                onClick={swapCurrencies}
                shape="circle"
                size="large"
              />
            </Tooltip>
            <Button
              type="primary"
              onClick={handleConvert}
              disabled={loading}
              size="large"
              icon={loading ? <SyncOutlined spin /> : <DollarOutlined />}
            >
              {loading ? 'Converting...' : 'Convert'}
            </Button>
          </div>

          {error && (
            <Alert
              message="Error"
              description={error}
              type="error"
              showIcon
              closable
            />
          )}

          {result && (
            <Space direction="vertical" size="large" className="w-full">
              <ConversionResult
                amount={amount}
                sourceCurrency={sourceCurrency}
                targetCurrency={targetCurrency}
                result={result}
                lastUpdated={lastUpdated}
                getCurrencySymbol={getCurrencySymbol}
              />

              <HistoricalChart
                historicalData={historicalData}
                timeRange={timeRange}
                onTimeRangeChange={setTimeRange}
              />

              {recentSearches.length > 0 && (
                <RecentSearches searches={recentSearches} />
              )}
            </Space>
          )}
        </Space>
      </Card>
    </div>
  );
};

export default CurrencyConverter;