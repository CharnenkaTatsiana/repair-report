import { useState, useEffect } from 'react';
import api from '../services/api.js';

export const useEnterprises = () => {
    const [enterprises, setEnterprises] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchEnterprises = async () => {
        try {
            setLoading(true);
            const response = await api.get('/enterprises');
            setEnterprises(response.data);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEnterprises();
    }, []);

    return { enterprises, loading, error, refetch: fetchEnterprises };
};