import React from 'react';
import { Row, Col, Card, Statistic, Progress, Table, Tag } from 'antd';
import {
    BarChartOutlined,
    FileTextOutlined,
    CheckCircleOutlined,
    ClockCircleOutlined
} from '@ant-design/icons';
import { usePlans } from '../../hooks/usePlans';
import { useReports } from '../../hooks/useReports';

const Dashboard = () => {
    const { data: plans, loading: plansLoading } = usePlans();
    const { data: reports, loading: reportsLoading } = useReports();

    const recentPlans = plans?.slice(0, 5) || [];
    const recentReports = reports?.slice(0, 5) || [];

    const planStatusCounts = plans?.reduce((acc, plan) => {
        acc[plan.status] = (acc[plan.status] || 0) + 1;
        return acc;
    }, {}) || {};

    const reportStatusCounts = reports?.reduce((acc, report) => {
        acc[report.status] = (acc[report.status] || 0) + 1;
        return acc;
    }, {}) || {};

    const planColumns = [
        {
            title: 'Филиал',
            dataIndex: 'branchName',
            key: 'branchName',
        },
        {
            title: 'Год',
            dataIndex: 'year',
            key: 'year',
        },
        {
            title: 'Статус',
            dataIndex: 'status',
            key: 'status',
            render: (status) => (
                <Tag color={status === 'APPROVED' ? 'green' : status === 'DRAFT' ? 'blue' : 'orange'}>
                    {status === 'APPROVED' ? 'Утвержден' : status === 'DRAFT' ? 'Черновик' : 'Отправлен'}
                </Tag>
            ),
        },
    ];

    const reportColumns = [
        {
            title: 'Филиал',
            dataIndex: 'branchName',
            key: 'branchName',
        },
        {
            title: 'Период',
            dataIndex: 'period',
            key: 'period',
            render: (period) => period ? new Date(period).toLocaleDateString('ru-RU') : '-',
        },
        {
            title: 'Статус',
            dataIndex: 'status',
            key: 'status',
            render: (status) => (
                <Tag color={status === 'SENT' ? 'green' : 'blue'}>
                    {status === 'SENT' ? 'Отправлен' : 'Черновик'}
                </Tag>
            ),
        },
    ];

    return (
        <div>
            <Row gutter={[16, 16]}>
                <Col xs={24} sm={12} md={6}>
                    <Card>
                        <Statistic
                            title="Всего планов"
                            value={plans?.length || 0}
                            prefix={<FileTextOutlined />}
                        />
                    </Card>
                </Col>
                <Col xs={24} sm={12} md={6}>
                    <Card>
                        <Statistic
                            title="Всего отчетов"
                            value={reports?.length || 0}
                            prefix={<BarChartOutlined />}
                        />
                    </Card>
                </Col>
                <Col xs={24} sm={12} md={6}>
                    <Card>
                        <Statistic
                            title="Утвержденные планы"
                            value={planStatusCounts.APPROVED || 0}
                            prefix={<CheckCircleOutlined />}
                        />
                    </Card>
                </Col>
                <Col xs={24} sm={12} md={6}>
                    <Card>
                        <Statistic
                            title="Отчеты в работе"
                            value={reportStatusCounts.DRAFT || 0}
                            prefix={<ClockCircleOutlined />}
                        />
                    </Card>
                </Col>
            </Row>

            <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
                <Col xs={24} md={12}>
                    <Card title="Последние планы" loading={plansLoading}>
                        <Table
                            dataSource={recentPlans}
                            columns={planColumns}
                            pagination={false}
                            size="small"
                            rowKey="id"
                        />
                    </Card>
                </Col>
                <Col xs={24} md={12}>
                    <Card title="Последние отчеты" loading={reportsLoading}>
                        <Table
                            dataSource={recentReports}
                            columns={reportColumns}
                            pagination={false}
                            size="small"
                            rowKey="id"
                        />
                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default Dashboard;