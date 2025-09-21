import React, { useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select, Switch, message, Card, Tag } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { useApi } from '../../hooks/useApi.js';
import api from '../../services/api.js';

const { Option } = Select;

const WorkCategoriesList = () => {
    const [modalVisible, setModalVisible] = useState(false);
    const [editingCategory, setEditingCategory] = useState(null);
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const { data: categories, loading: categoriesLoading, refetch } = useApi('/work-categories');

    const handleCreate = () => {
        setEditingCategory(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (category) => {
        setEditingCategory(category);
        form.setFieldsValue({
            ...category,
            networkType: category.networkType.toLowerCase()
        });
        setModalVisible(true);
    };

    const handleDelete = async (id) => {
        Modal.confirm({
            title: 'Удалить категорию работ?',
            content: 'Категория будет деактивирована.',
            okText: 'Удалить',
            okType: 'danger',
            cancelText: 'Отмена',
            onOk: async () => {
                try {
                    await api.delete(`/work-categories/${id}`);
                    message.success('Категория работ удалена');
                    refetch();
                } catch (error) {
                    message.error('Ошибка при удалении: ' + error.message);
                }
            },
        });
    };

    const handleSubmit = async (values) => {
        setLoading(true);
        try {
            const categoryData = {
                ...values,
                networkType: values.networkType.toUpperCase()
            };

            if (editingCategory) {
                await api.put(`/work-categories/${editingCategory.id}`, categoryData);
                message.success('Категория работ обновлена');
            } else {
                await api.post('/work-categories', categoryData);
                message.success('Категория работ создана');
            }
            setModalVisible(false);
            refetch();
        } catch (error) {
            message.error('Ошибка: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const columns = [
        {
            title: 'Название',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Тип сети',
            dataIndex: 'networkType',
            key: 'networkType',
            render: (type) => (
                <Tag color={type === 'MAIN' ? 'blue' : 'green'}>
                    {type === 'MAIN' ? 'Основная' : 'Распределительная'}
                </Tag>
            ),
        },
        {
            title: 'Статус',
            dataIndex: 'isActive',
            key: 'isActive',
            render: (active) => (
                <Tag color={active ? 'green' : 'red'}>
                    {active ? 'Активна' : 'Неактивна'}
                </Tag>
            ),
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_, record) => (
                <Space size="middle">
                    <Button
                        type="link"
                        icon={<EditOutlined />}
                        onClick={() => handleEdit(record)}
                    >
                        Редактировать
                    </Button>
                    <Button
                        type="link"
                        danger
                        icon={<DeleteOutlined />}
                        onClick={() => handleDelete(record.id)}
                    >
                        Удалить
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <Card
            title="Категории работ"
            extra={
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={handleCreate}
                >
                    Добавить категорию
                </Button>
            }
        >
            <Table
                columns={columns}
                dataSource={categories}
                loading={categoriesLoading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />

            <Modal
                title={editingCategory ? 'Редактировать категорию' : 'Новая категория'}
                open={modalVisible}
                onCancel={() => setModalVisible(false)}
                footer={null}
                width={600}
            >
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={handleSubmit}
                >
                    <Form.Item
                        name="name"
                        label="Название работы"
                        rules={[{ required: true, message: 'Введите название работы!' }]}
                    >
                        <Input placeholder="Введите название работы" />
                    </Form.Item>

                    <Form.Item
                        name="networkType"
                        label="Тип сети"
                        rules={[{ required: true, message: 'Выберите тип сети!' }]}
                    >
                        <Select placeholder="Выберите тип сети">
                            <Option value="main">Основная сеть</Option>
                            <Option value="distribution">Распределительная сеть</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="isActive"
                        label="Активна"
                        valuePropName="checked"
                    >
                        <Switch />
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button type="primary" htmlType="submit" loading={loading}>
                                {editingCategory ? 'Обновить' : 'Создать'}
                            </Button>
                            <Button onClick={() => setModalVisible(false)}>
                                Отмена
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Modal>
        </Card>
    );
};

export default WorkCategoriesList;