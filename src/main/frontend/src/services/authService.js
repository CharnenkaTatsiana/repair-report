import api from './api';

export const authService = {
    login: async (username, password) => {
        try {
            console.log('Attempting login with:', username);

            const response = await api.post('/auth/signin', {
                username,
                password
            });

            console.log('Login successful:', response.data);
            return response.data;

        } catch (error) {
            console.error('Login failed:', error.response?.data || error.message);
            throw error;
        }
    },

    getCurrentUser: async () => {
        try {
            const response = await api.get('/admin/users/me');
            return response.data;
        } catch (error) {
            console.error('Get current user failed:', error);
            throw error;
        }
    }
};