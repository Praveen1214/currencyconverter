// hooks/useRecentSearches.js
import { useState } from 'react';

export const useRecentSearches = (maxSearches = 5) => {
  const [recentSearches, setRecentSearches] = useState([]);

  const addRecentSearch = (search) => {
    setRecentSearches(prev => [search, ...prev].slice(0, maxSearches));
  };

  return {
    recentSearches,
    addRecentSearch
  };
};