// components/HistoricalChart.js
import React from 'react';
import { Card, Radio, Typography } from 'antd';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

const { Text } = Typography;

export const HistoricalChart = ({ historicalData, timeRange, onTimeRangeChange }) => (
  <Card
    title={
      <div className="flex justify-between items-center">
        <Text strong>Historical Rates</Text>
        <Radio.Group 
          value={timeRange}
          onChange={(e) => onTimeRangeChange(e.target.value)}
          size="small"
        >
          <Radio.Button value="week">Week</Radio.Button>
          <Radio.Button value="month">Month</Radio.Button>
          <Radio.Button value="quarter">Quarter</Radio.Button>
        </Radio.Group>
      </div>
    }
  >
    <div className="h-64">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={historicalData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" stroke="#6B7280" fontSize={12} />
          <YAxis stroke="#6B7280" fontSize={12} domain={['auto', 'auto']} />
          <Tooltip />
          <Line 
            type="monotone" 
            dataKey="rate" 
            stroke="#1890ff" 
            strokeWidth={2}
            dot={{ fill: '#1890ff' }}
            name="Exchange Rate"
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  </Card>
);