import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Простая проверка токена
        const token = localStorage.getItem('token');
        if (token) {
            // Mock пользователь для тестирования
            setUser({
                id: 1,
                username: 'admin',
                roles: ['ROLE_ADMIN']
            });
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        try {
            // Mock логин для тестирования
            if (username === 'admin' && password === 'admin') {
                localStorage.setItem('token', 'mock-token-123');
                const userInfo = {
                    id: 1,
                    username: 'admin',
                    roles: ['ROLE_ADMIN']
                };
                setUser(userInfo);
                return userInfo;
            }
            throw new Error('Неверные учетные данные');
        } catch (error) {
            throw new Error(error.message);
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
        window.location.href = '/login';
    };

    const value = {
        user,
        login,
        logout,
        loading,
        isAuthenticated: !!user
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};