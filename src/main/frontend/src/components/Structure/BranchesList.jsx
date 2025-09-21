import React, { useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select, message, Card } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { useApi } from '../../hooks/useApi.js';
import api from '../../services/api.js';

const { Option } = Select;

const BranchesList = () => {
    const [modalVisible, setModalVisible] = useState(false);
    const [editingBranch, setEditingBranch] = useState(null);
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const { data: branches, loading: branchesLoading, refetch } = useApi('/branches');
    const { data: enterprises } = useApi('/enterprises');

    const handleCreate = () => {
        setEditingBranch(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (branch) => {
        setEditingBranch(branch);
        form.setFieldsValue(branch);
        setModalVisible(true);
    };

    const handleDelete = async (id) => {
        Modal.confirm({
            title: 'Удалить филиал?',
            content: 'Это действие нельзя отменить.',
            okText: 'Удалить',
            okType: 'danger',
            cancelText: 'Отмена',
            onOk: async () => {
                try {
                    await api.delete(`/branches/${id}`);
                    message.success('Филиал удален');
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
            if (editingBranch) {
                await api.put(`/branches/${editingBranch.id}`, values);
                message.success('Филиал обновлен');
            } else {
                await api.post('/branches', values);
                message.success('Филиал создан');
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
            title: 'Предприятие',
            dataIndex: 'enterpriseName',
            key: 'enterpriseName',
        },
        {
            title: 'Объединение',
            dataIndex: 'associationName',
            key: 'associationName',
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
            title="Филиалы"
            extra={
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={handleCreate}
                >
                    Добавить филиал
                </Button>
            }
        >
            <Table
                columns={columns}
                dataSource={branches}
                loading={branchesLoading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />

            <Modal
                title={editingBranch ? 'Редактировать филиал' : 'Новый филиал'}
                open={modalVisible}
                onCancel={() => setModalVisible(false)}
                footer={null}
            >
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={handleSubmit}
                >
                    <Form.Item
                        name="name"
                        label="Название филиала"
                        rules={[{ required: true, message: 'Введите название филиала!' }]}
                    >
                        <Input placeholder="Введите название" />
                    </Form.Item>

                    <Form.Item
                        name="enterpriseId"
                        label="Предприятие"
                        rules={[{ required: true, message: 'Выберите предприятие!' }]}
                    >
                        <Select placeholder="Выберите предприятие">
                            {enterprises?.map(enterprise => (
                                <Option key={enterprise.id} value={enterprise.id}>
                                    {enterprise.name}
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button type="primary" htmlType="submit" loading={loading}>
                                {editingBranch ? 'Обновить' : 'Создать'}
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

export default BranchesList;