// components/ConversionResult.js
import React from 'react';
import { Card, Space, Statistic, Typography } from 'antd';

const { Text } = Typography;

export const ConversionResult = ({
  amount,
  sourceCurrency,
  targetCurrency,
  result,
  lastUpdated,
  getCurrencySymbol
}) => (
  <Card className="text-center">
    <Space direction="vertical" size="small">
      <Statistic
        title={<Text strong>{parseFloat(amount).toLocaleString()} {sourceCurrency}</Text>}
        value={parseFloat(result.convertedAmount).toLocaleString()}
        prefix={getCurrencySymbol(targetCurrency)}
        suffix={targetCurrency}
        precision={2}
      />
      <Text type="secondary">
        1 {sourceCurrency} = {result.exchangeRate} {targetCurrency}
      </Text>
      <Text type="secondary" className="text-xs">
        Last updated: {lastUpdated.toLocaleString()}
      </Text>
    </Space>
  </Card>
);