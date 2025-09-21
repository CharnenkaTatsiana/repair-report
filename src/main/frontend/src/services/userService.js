import api from './api.js';

export const userService = {
    getAllUsers: async () => {
        const response = await api.get('/admin/users');
        return response.data;
    },

    getUserById: async (id) => {
        const response = await api.get(`/admin/users/${id}`);
        return response.data;
    },

    createUser: async (userData) => {
        const response = await api.post('/admin/users', userData);
        return response.data;
    },

    updateUser: async (id, userData) => {
        const response = await api.put(`/admin/users/${id}`, userData);
        return response.data;
    },

    deleteUser: async (id) => {
        await api.delete(`/admin/users/${id}`);
    },

    changePassword: async (id, passwordData) => {
        await api.put(`/admin/users/${id}/password`, passwordData);
    }
};