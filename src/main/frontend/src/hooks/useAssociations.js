import { useState, useEffect } from 'react';
import api from '../services/api.js';

export const useAssociations = () => {
    const [associations, setAssociations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchAssociations = async () => {
        try {
            setLoading(true);
            const response = await api.get('/associations');
            setAssociations(response.data);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAssociations();
    }, []);

    return { associations, loading, error, refetch: fetchAssociations };
};