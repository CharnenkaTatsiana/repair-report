import React, { useState } from 'react';
import { Layout, Menu, Button, Avatar, Dropdown } from 'antd';
import {
    DashboardOutlined,
    FileTextOutlined,
    BarChartOutlined,
    TeamOutlined,
    SettingOutlined,
    UserOutlined,
    LogoutOutlined
} from '@ant-design/icons';
import { useAuth } from '../../contexts/AuthContext.jsx';
import './MainLayout.css';

const { Header, Sider, Content } = Layout;

const MainLayout = ({ children }) => {
    const [collapsed, setCollapsed] = useState(false);
    const { user, logout } = useAuth();

    const menuItems = [
        {
            key: 'dashboard',
            icon: <DashboardOutlined />,
            label: 'Дашборд',
        },
        {
            key: 'plans',
            icon: <FileTextOutlined />,
            label: 'Планы',
            children: [
                { key: 'plans-list', label: 'Все планы' },
                { key: 'create-plan', label: 'Создать план' },
            ],
        },
        {
            key: 'reports',
            icon: <BarChartOutlined />,
            label: 'Отчеты',
            children: [
                { key: 'reports-list', label: 'Все отчеты' },
                { key: 'create-report', label: 'Создать отчет' },
            ],
        },
        {
            key: 'structure',
            icon: <TeamOutlined />,
            label: 'Структура',
            children: [
                { key: 'associations', label: 'Объединения' },
                { key: 'enterprises', label: 'Предприятия' },
                { key: 'branches', label: 'Филиалы' },
            ],
        },
    ];

    if (user?.roles?.includes('ROLE_ADMIN') || user?.roles?.includes('ROLE_ASSOCIATION_ENG')) {
        menuItems.push({
            key: 'admin',
            icon: <SettingOutlined />,
            label: 'Администрирование',
            children: [
                { key: 'users', label: 'Пользователи' },
                { key: 'work-categories', label: 'Категории работ' },
            ],
        });
    }

    const userMenuItems = [
        {
            key: 'profile',
            icon: <UserOutlined />,
            label: 'Профиль',
        },
        {
            key: 'logout',
            icon: <LogoutOutlined />,
            label: 'Выйти',
            onClick: logout,
        },
    ];

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider
                collapsible
                collapsed={collapsed}
                onCollapse={setCollapsed}
                theme="dark"
            >
                <div className="logo">
                    <h2>{collapsed ? 'RR' : 'Repair Reports'}</h2>
                </div>
                <Menu
                    theme="dark"
                    defaultSelectedKeys={['dashboard']}
                    mode="inline"
                    items={menuItems}
                />
            </Sider>

            <Layout>
                <Header style={{
                    padding: '0 24px',
                    background: '#fff',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                }}>
                    <h2>Система планов и отчетов</h2>

                    <Dropdown
                        menu={{ items: userMenuItems }}
                        placement="bottomRight"
                        arrow
                    >
                        <Button type="text" style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                            <Avatar size="small" icon={<UserOutlined />} />
                            <span>{user?.username}</span>
                        </Button>
                    </Dropdown>
                </Header>

                <Content style={{
                    margin: '24px 16px',
                    padding: 24,
                    background: '#fff',
                    minHeight: 280
                }}>
                    {children}
                </Content>
            </Layout>
        </Layout>
    );
};

export default MainLayout;