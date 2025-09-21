import React, { useState, useEffect } from 'react';
import { Form, Input, Select, Button, Card, Row, Col, Table, InputNumber, message } from 'antd';
import { useBranches } from '../../hooks/useBranches.js';
import { useWorkCategories } from '../../hooks/useWorkCategories.js';
import { planService } from '../../services/planService.js';

const { Option } = Select;

const PlanForm = ({ editMode, initialData, onSuccess }) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const { branches, loading: branchesLoading } = useBranches();
    const { workCategories, loading: categoriesLoading } = useWorkCategories();

    const [planItems, setPlanItems] = useState([]);

    useEffect(() => {
        if (initialData) {
            form.setFieldsValue(initialData);
            setPlanItems(initialData.planItems || []);
        }
    }, [initialData, form]);

    const handlePlanItemChange = (workCategoryId, field, value) => {
        const updatedItems = planItems.map(item =>
            item.workCategoryId === workCategoryId
                ? { ...item, [field]: value }
                : item
        );

        if (!updatedItems.find(item => item.workCategoryId === workCategoryId)) {
            updatedItems.push({
                workCategoryId,
                [field]: value
            });
        }

        setPlanItems(updatedItems);
    };

    const calculateAnnual = (q1, q2, q3, q4) => {
        return (q1 || 0) + (q2 || 0) + (q3 || 0) + (q4 || 0);
    };

    const onFinish = async (values) => {
        setLoading(true);
        try {
            const planData = {
                ...values,
                planItems: planItems.reduce((acc, item) => {
                    if (item.q1 || item.q2 || item.q3 || item.q4) {
                        acc[item.workCategoryId] = {
                            q1: item.q1 || 0,
                            q2: item.q2 || 0,
                            q3: item.q3 || 0,
                            q4: item.q4 || 0
                        };
                    }
                    return acc;
                }, {})
            };

            if (editMode && initialData?.id) {
                await planService.updatePlan(initialData.id, planData);
                message.success('План успешно обновлен!');
            } else {
                await planService.createPlan(planData);
                message.success('План успешно создан!');
            }

            onSuccess?.();
        } catch (error) {
            message.error('Ошибка: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const mainNetworkCategories = workCategories.filter(cat => cat.networkType === 'MAIN');
    const distributionNetworkCategories = workCategories.filter(cat => cat.networkType === 'DISTRIBUTION');

    const renderPlanTable = (categories, title) => (
        <Card title={title} size="small" style={{ marginBottom: 16 }}>
            <Table
                dataSource={categories}
                pagination={false}
                size="small"
                columns={[
                    {
                        title: 'Наименование работ',
                        dataIndex: 'name',
                        key: 'name',
                        width: '40%',
                    },
                    {
                        title: 'Квартал 1',
                        dataIndex: 'q1',
                        key: 'q1',
                        render: (_, record) => (
                            <InputNumber
                                min={0}
                                placeholder="0"
                                value={planItems.find(item => item.workCategoryId === record.id)?.q1}
                                onChange={(value) => handlePlanItemChange(record.id, 'q1', value)}
                                style={{ width: '100%' }}
                            />
                        ),
                    },
                    {
                        title: 'Квартал 2',
                        dataIndex: 'q2',
                        key: 'q2',
                        render: (_, record) => (
                            <InputNumber
                                min={0}
                                placeholder="0"
                                value={planItems.find(item => item.workCategoryId === record.id)?.q2}
                                onChange={(value) => handlePlanItemChange(record.id, 'q2', value)}
                                style={{ width: '100%' }}
                            />
                        ),
                    },
                    {
                        title: 'Квартал 3',
                        dataIndex: 'q3',
                        key: 'q3',
                        render: (_, record) => (
                            <InputNumber
                                min={0}
                                placeholder="0"
                                value={planItems.find(item => item.workCategoryId === record.id)?.q3}
                                onChange={(value) => handlePlanItemChange(record.id, 'q3', value)}
                                style={{ width: '100%' }}
                            />
                        ),
                    },
                    {
                        title: 'Квартал 4',
                        dataIndex: 'q4',
                        key: 'q4',
                        render: (_, record) => (
                            <InputNumber
                                min={0}
                                placeholder="0"
                                value={planItems.find(item => item.workCategoryId === record.id)?.q4}
                                onChange={(value) => handlePlanItemChange(record.id, 'q4', value)}
                                style={{ width: '100%' }}
                            />
                        ),
                    },
                    {
                        title: 'Годовой план',
                        key: 'annual',
                        render: (_, record) => {
                            const item = planItems.find(i => i.workCategoryId === record.id);
                            const annual = calculateAnnual(item?.q1, item?.q2, item?.q3, item?.q4);
                            return <span>{annual || 0}</span>;
                        },
                    },
                ]}
            />
        </Card>
    );

    return (
        <Form
            form={form}
            layout="vertical"
            onFinish={onFinish}
            initialValues={{ year: new Date().getFullYear() }}
        >
            <Row gutter={16}>
                <Col xs={24} md={12}>
                    <Form.Item
                        name="branchId"
                        label="Филиал"
                        rules={[{ required: true, message: 'Выберите филиал!' }]}
                    >
                        <Select
                            placeholder="Выберите филиал"
                            loading={branchesLoading}
                            disabled={editMode}
                        >
                            {branches.map(branch => (
                                <Option key={branch.id} value={branch.id}>
                                    {branch.name}
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>
                </Col>
                <Col xs={24} md={12}>
                    <Form.Item
                        name="year"
                        label="Год"
                        rules={[{ required: true, message: 'Введите год!' }]}
                    >
                        <InputNumber
                            min={2020}
                            max={2030}
                            style={{ width: '100%' }}
                            placeholder="Год"
                        />
                    </Form.Item>
                </Col>
            </Row>

            {renderPlanTable(mainNetworkCategories, 'Основная сеть')}
            {renderPlanTable(distributionNetworkCategories, 'Распределительная сеть')}

            <Form.Item>
                <Button type="primary" htmlType="submit" loading={loading} size="large">
                    {editMode ? 'Обновить план' : 'Создать план'}
                </Button>
            </Form.Item>
        </Form>
    );
};

export default PlanForm;