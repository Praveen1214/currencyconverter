// components/CurrencySelect.js
import React from 'react';
import { Select, Typography } from 'antd';
import { commonCurrencies } from '../constants/currencies';

const { Text } = Typography;

export const CurrencySelect = ({ label, value, onChange }) => (
  <div>
    <Text strong className="mb-1 block">{label}</Text>
    <Select
      value={value}
      onChange={onChange}
      className="w-full"
      size="large"
      showSearch
      optionFilterProp="children"
    >
      {commonCurrencies.map((currency) => (
        <Select.Option key={currency.code} value={currency.code}>
          {currency.flag} {currency.code} - {currency.name}
        </Select.Option>
      ))}
    </Select>
  </div>
);