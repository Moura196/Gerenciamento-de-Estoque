import axios from 'axios';
import AuthService from './auth.service';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
});

api.interceptors.request.use(config => {
  const token = AuthService.getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      AuthService.removeToken();
      window.location.href = '/login.html?sessionExpired=true';
    }
    return Promise.reject(error);
  }
);

export default api;