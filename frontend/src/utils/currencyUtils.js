import { commonCurrencies } from '../constants/currencies';

export const getCurrencySymbol = (code) => {
  const currency = commonCurrencies.find(c => c.code === code);
  return currency ? currency.symbol : '';
};