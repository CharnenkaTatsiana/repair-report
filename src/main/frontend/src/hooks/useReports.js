import { useApi } from './useApi';

export const useReports = () => {
    return useApi('/reports');
};