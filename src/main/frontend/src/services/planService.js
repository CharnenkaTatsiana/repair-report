import api from './api.js';

export const planService = {
    getAllPlans: async () => {
        const response = await api.get('/plans');
        return response.data;
    },

    getPlanById: async (id) => {
        const response = await api.get(`/plans/${id}`);
        return response.data;
    },

    createPlan: async (planData) => {
        const response = await api.post('/plans', planData);
        return response.data;
    },

    updatePlan: async (id, planData) => {
        const response = await api.put(`/plans/${id}`, planData);
        return response.data;
    },

    deletePlan: async (id) => {
        await api.delete(`/plans/${id}`);
    }
};