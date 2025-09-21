import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
});

// Логируем все запросы
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    config.credentials = 'include'; // ← Добавьте эту строку!
    return config;
});

// Логируем все ответы
api.interceptors.response.use(
    (response) => {
        console.log('API Response Success:', response.status, response.data);
        return response;
    },
    (error) => {
        console.error('API Response Error:', error.response?.status);
        console.error('Error Data:', error.response?.data);
        console.error('Error Message:', error.message);

        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }

        return Promise.reject(error);
    }
);

export default api;