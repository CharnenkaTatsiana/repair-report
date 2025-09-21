import React, { useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, message, Card } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { useAssociations } from '../../hooks/useAssociations.js';
import { associationService } from '../../services/associationService.js';

const AssociationsList = () => {
    const [modalVisible, setModalVisible] = useState(false);
    const [editingAssociation, setEditingAssociation] = useState(null);
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const { associations, loading: associationsLoading, refetch } = useAssociations();

    const handleCreate = () => {
        setEditingAssociation(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (association) => {
        setEditingAssociation(association);
        form.setFieldsValue(association);
        setModalVisible(true);
    };

    const handleDelete = async (id) => {
        Modal.confirm({
            title: 'Удалить объединение?',
            content: 'Это действие нельзя отменить.',
            okText: 'Удалить',
            okType: 'danger',
            cancelText: 'Отмена',
            onOk: async () => {
                try {
                    await associationService.deleteAssociation(id);
                    message.success('Объединение удалено');
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
            if (editingAssociation) {
                await associationService.updateAssociation(editingAssociation.id, values);
                message.success('Объединение обновлено');
            } else {
                await associationService.createAssociation(values);
                message.success('Объединение создано');
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
            title: 'Количество предприятий',
            dataIndex: 'enterpriseCount',
            key: 'enterpriseCount',
            render: (count) => count || 0,
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
            title="Объединения"
            extra={
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={handleCreate}
                >
                    Добавить объединение
                </Button>
            }
        >
            <Table
                columns={columns}
                dataSource={associations}
                loading={associationsLoading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />

            <Modal
                title={editingAssociation ? 'Редактировать объединение' : 'Новое объединение'}
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
                        label="Название объединения"
                        rules={[{ required: true, message: 'Введите название объединения!' }]}
                    >
                        <Input placeholder="Введите название" />
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button type="primary" htmlType="submit" loading={loading}>
                                {editingAssociation ? 'Обновить' : 'Создать'}
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

export default AssociationsList;