// components/RecentSearches.js
import React from 'react';
import { Card, Space, Typography } from 'antd';
import { HistoryOutlined } from '@ant-design/icons';

const { Text } = Typography;

export const RecentSearches = ({ searches }) => (
  <Card 
    title={
      <Space>
        <HistoryOutlined />
        <Text strong>Recent Conversions</Text>
      </Space>
    }
    size="small"
  >
    {searches.map((search, index) => (
      <div key={index} className="flex justify-between items-center mb-2">
        <Text>
          {parseFloat(search.amount).toLocaleString()} {search.from} → {' '}
          {parseFloat(search.result).toLocaleString()} {search.to}
        </Text>
        <Text type="secondary" className="text-xs">
          {new Date(search.timestamp).toLocaleTimeString()}
        </Text>
      </div>
    ))}
  </Card>
);