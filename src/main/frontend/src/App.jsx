import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ConfigProvider, Spin } from 'antd';
import ruRU from 'antd/locale/ru_RU';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Login from './components/Auth/Login';
import Dashboard from './components/Dashboard/Dashboard';

// Упрощенный ProtectedRoute
const ProtectedRoute = ({ children }) => {
    const { isAuthenticated, loading } = useAuth();

    if (loading) {
        return <Spin size="large" />;
    }

    return isAuthenticated ? children : <Navigate to="/login" replace />;
};

// Упрощенный AppRoutes
const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />

            <Route path="/dashboard" element={
                <ProtectedRoute>
                    <div>
                        <h1>Dashboard</h1>
                        <p>Привет! Вы успешно вошли в систему.</p>
                    </div>
                </ProtectedRoute>
            } />

            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
    );
};

function App() {
    return (
        <ConfigProvider locale={ruRU}>
            <AuthProvider>
                <Router>
                    <AppRoutes />
                </Router>
            </AuthProvider>
        </ConfigProvider>
    );
}

export default App;