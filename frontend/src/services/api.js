import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const API_BASE_URL = 'https://expensetrackerappbackend.onrender.com/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests automatically
api.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('userToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Auth APIs
export const authAPI = {
  signup: (data) => api.post('/auth/signup', data),
  login: (data) => api.post('/auth/login', data),
};

// Transaction APIs
export const transactionAPI = {
  create: (data) => api.post('/transactions', data),
  getAll: () => api.get('/transactions'),
  getByType: (type) => api.get(`/transactions/type/${type}`),
  getByCategory: (category) => api.get(`/transactions/category/${category}`),
  getByDateRange: (startDate, endDate) => 
    api.get(`/transactions/date-range?startDate=${startDate}&endDate=${endDate}`),
  getCurrentMonthSummary: () => api.get('/transactions/summary/current'),
  getMonthSummary: (year, month) => api.get(`/transactions/summary/${year}/${month}`),
  delete: (id) => api.delete(`/transactions/${id}`),
};

export default api;