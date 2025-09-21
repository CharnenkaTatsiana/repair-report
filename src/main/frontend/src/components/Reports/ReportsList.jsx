import React from 'react';
import { Table, Card, Tag } from 'antd';
import { useApi } from '../../hooks/useApi.js';

const ReportsList = () => {
    const { data: reports, loading } = useApi('/reports');

    const columns = [
        {
            title: 'Филиал',
            dataIndex: 'branchName',
            key: 'branchName',
        },
        {
            title: 'Период',
            dataIndex: 'period',
            key: 'period',
            render: (period) => new Date(period).toLocaleDateString('ru-RU'),
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
        <Card title="Отчеты">
            <Table
                columns={columns}
                dataSource={reports}
                loading={loading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />
        </Card>
    );
};

export default ReportsList;