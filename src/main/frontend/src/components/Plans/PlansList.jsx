import React from 'react';
import { Table, Card, Tag } from 'antd';
import { usePlans } from '../../hooks/usePlans.js';

const PlansList = () => {
    const { data: plans, loading } = usePlans();

    const columns = [
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

    return (
        <Card title="Планы">
            <Table
                columns={columns}
                dataSource={plans}
                loading={loading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />
        </Card>
    );
};

export default PlansList;