import React from 'react';
import { Card, Typography } from 'antd';

const { Title, Paragraph } = Typography;

export default function HomePage() {
    return (
        <Card>
            <Title level={1}>Repair Report System</Title>
            <Paragraph>Система учета ремонтных работ</Paragraph>
            <Paragraph>Бэкенд: http://localhost:8080</Paragraph>
            <Paragraph>Фронтенд: http://localhost:3000</Paragraph>
        </Card>
    );
}