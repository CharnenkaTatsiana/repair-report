import { useApi } from './useApi';

export const usePlans = () => {
    return useApi('/plans');
};