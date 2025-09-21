import api from './api';

export const enterpriseService = {
    getAllEnterprises: async () => {
        const response = await api.get('/enterprises');
        return response.data;
    },

    getEnterpriseById: async (id) => {
        const response = await api.get(`/enterprises/${id}`);
        return response.data;
    },

    createEnterprise: async (enterpriseData) => {
        const response = await api.post('/enterprises', enterpriseData);
        return response.data;
    },

    updateEnterprise: async (id, enterpriseData) => {
        const response = await api.put(`/enterprises/${id}`, enterpriseData);
        return response.data;
    },

    deleteEnterprise: async (id) => {
        await api.delete(`/enterprises/${id}`);
    },

    getEnterprisesByAssociation: async (associationId) => {
        const response = await api.get(`/enterprises/association/${associationId}`);
        return response.data;
    },

    searchEnterprises: async (associationId, name) => {
        const response = await api.get(`/enterprises/search?associationId=${associationId}&name=${name}`);
        return response.data;
    }
};
