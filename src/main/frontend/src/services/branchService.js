import api from './api';

export const branchService = {
    getAllBranches: async () => {
        const response = await api.get('/branches');
        return response.data;
    },

    getBranchById: async (id) => {
        const response = await api.get(`/branches/${id}`);
        return response.data;
    },

    createBranch: async (branchData) => {
        const response = await api.post('/branches', branchData);
        return response.data;
    },

    updateBranch: async (id, branchData) => {
        const response = await api.put(`/branches/${id}`, branchData);
        return response.data;
    },

    deleteBranch: async (id) => {
        await api.delete(`/branches/${id}`);
    },

    getBranchesByEnterprise: async (enterpriseId) => {
        const response = await api.get(`/branches/enterprise/${enterpriseId}`);
        return response.data;
    },

    getBranchesByAssociation: async (associationId) => {
        const response = await api.get(`/branches/association/${associationId}`);
        return response.data;
    },

    searchBranches: async (enterpriseId, name) => {
        const response = await api.get(`/branches/search?enterpriseId=${enterpriseId}&name=${name}`);
        return response.data;
    }
};