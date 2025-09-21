import React, { useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, message, Card } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { useApi } from '../../hooks/useApi.js';
import api from '../../services/api.js';

const EnterprisesList = () => {
    const [modalVisible, setModalVisible] = useState(false);
    const [editingEnterprise, setEditingEnterprise] = useState(null);
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const { data: enterprises, loading: enterprisesLoading, refetch } = useApi('/enterprises');
    const { data: associations } = useApi('/associations');

    const handleCreate = () => {
        setEditingEnterprise(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (enterprise) => {
        setEditingEnterprise(enterprise);
        form.setFieldsValue(enterprise);
        setModalVisible(true);
    };

    const handleDelete = async (id) => {
        Modal.confirm({
            title: 'Удалить предприятие?',
            content: 'Это действие нельзя отменить.',
            okText: 'Удалить',
            okType: 'danger',
            cancelText: 'Отмена',
            onOk: async () => {
                try {
                    await api.delete(`/enterprises/${id}`);
                    message.success('Предприятие удалено');
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
            if (editingEnterprise) {
                await api.put(`/enterprises/${editingEnterprise.id}`, values);
                message.success('Предприятие обновлено');
            } else {
                await api.post('/enterprises', values);
                message.success('Предприятие создано');
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
            title: 'Объединение',
            dataIndex: 'associationName',
            key: 'associationName',
        },
        {
            title: 'Количество филиалов',
            dataIndex: 'branchCount',
            key: 'branchCount',
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
            title="Предприятия"
            extra={
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={handleCreate}
                >
                    Добавить предприятие
                </Button>
            }
        >
            <Table
                columns={columns}
                dataSource={enterprises}
                loading={enterprisesLoading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />

            <Modal
                title={editingEnterprise ? 'Редактировать предприятие' : 'Новое предприятие'}
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
                        label="Название предприятия"
                        rules={[{ required: true, message: 'Введите название предприятия!' }]}
                    >
                        <Input placeholder="Введите название" />
                    </Form.Item>

                    <Form.Item
                        name="associationId"
                        label="Объединение"
                        rules={[{ required: true, message: 'Выберите объединение!' }]}
                    >
                        <Select placeholder="Выберите объединение">
                            {associations?.map(association => (
                                <Select.Option key={association.id} value={association.id}>
                                    {association.name}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button type="primary" htmlType="submit" loading={loading}>
                                {editingEnterprise ? 'Обновить' : 'Создать'}
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

export default EnterprisesList;