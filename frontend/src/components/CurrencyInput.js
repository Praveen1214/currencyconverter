// components/CurrencyInput.js
import React from 'react';
import { Input, Typography, Space, Tooltip } from 'antd';
import { InfoCircleOutlined } from '@ant-design/icons';
import { CurrencySelect } from './CurrencySelect';

const { Text } = Typography;

export const CurrencyInput = ({
  amount,
  sourceCurrency,
  targetCurrency,
  onAmountChange,
  onSourceChange,
  onTargetChange,
  onKeyPress,
  getCurrencySymbol
}) => (
  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 items-center">
    <div>
      <Text strong className="mb-1 block">Amount</Text>
      <Input
        type="number"
        value={amount}
        onChange={(e) => onAmountChange(e.target.value)}
        onKeyPress={onKeyPress}
        prefix={getCurrencySymbol(sourceCurrency)}
        suffix={<Tooltip title="Enter the amount you want to convert"><InfoCircleOutlined /></Tooltip>}
        placeholder="Enter amount"
        min="0"
        size="large"
      />
    </div>

    <Space className="grid grid-cols-2 gap-2">
      <CurrencySelect
        label="From"
        value={sourceCurrency}
        onChange={onSourceChange}
      />
      <CurrencySelect
        label="To"
        value={targetCurrency}
        onChange={onTargetChange}
      />
    </Space>
  </div>
);