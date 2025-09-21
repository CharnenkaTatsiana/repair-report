import api from './api';

export const reportService = {
    getAllReports: async () => {
        const response = await api.get('/reports');
        return response.data;
    },

    getReportById: async (id) => {
        const response = await api.get(`/reports/${id}`);
        return response.data;
    },

    createReport: async (reportData) => {
        const response = await api.post('/reports', reportData);
        return response.data;
    },

    updateReport: async (id, reportData) => {
        const response = await api.put(`/reports/${id}`, reportData);
        return response.data;
    },

    deleteReport: async (id) => {
        await api.delete(`/reports/${id}`);
    },

    getReportByBranchAndPeriod: async (branchId, period) => {
        const response = await api.get(`/reports/branch/${branchId}/period/${period}`);
        return response.data;
    },

    getReportsByBranchAndYear: async (branchId, year) => {
        const response = await api.get(`/reports/branch/${branchId}/year/${year}`);
        return response.data;
    },

    updateReportStatus: async (id, statusData) => {
        const response = await api.patch(`/reports/${id}/status`, statusData);
        return response.data;
    },

    getReportSummary: async (id) => {
        const response = await api.get(`/reports/${id}/summary`);
        return response.data;
    }
};