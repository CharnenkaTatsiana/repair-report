import { useState, useEffect } from 'react';
import api from '../services/api.js';

export const useWorkCategories = () => {
    const [workCategories, setWorkCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchWorkCategories = async () => {
        try {
            setLoading(true);
            const response = await api.get('/work-categories');
            setWorkCategories(response.data);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchWorkCategories();
    }, []);

    return { workCategories, loading, error, refetch: fetchWorkCategories };
};