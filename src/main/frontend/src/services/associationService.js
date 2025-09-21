import api from './api';

export const associationService = {
    getAllAssociations: async () => {
        const response = await api.get('/associations');
        return response.data;
    },

    getAssociationById: async (id) => {
        const response = await api.get(`/associations/${id}`);
        return response.data;
    },

    createAssociation: async (associationData) => {
        const response = await api.post('/associations', associationData);
        return response.data;
    },

    updateAssociation: async (id, associationData) => {
        const response = await api.put(`/associations/${id}`, associationData);
        return response.data;
    },

    deleteAssociation: async (id) => {
        await api.delete(`/associations/${id}`);
    },

    searchAssociations: async (name) => {
        const response = await api.get(`/associations/search?name=${name}`);
        return response.data;
    }
};