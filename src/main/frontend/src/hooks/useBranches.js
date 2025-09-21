import { useState, useEffect } from 'react';
import api from '../services/api.js';

export const useBranches = () => {
    const [branches, setBranches] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchBranches = async () => {
        try {
            setLoading(true);
            const response = await api.get('/branches');
            setBranches(response.data);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchBranches();
    }, []);

    return { branches, loading, error, refetch: fetchBranches };
};