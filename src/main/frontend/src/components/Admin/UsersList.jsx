import React, { useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select, message, Card, Tag } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { useUsers } from '../../hooks/useUsers.js';
import { useBranches } from '../../hooks/useBranches.js';
import { useEnterprises } from '../../hooks/useEnterprises.js';
import { userService } from '../../services/userService.js';

const { Option } = Select;

const UsersList = () => {
    const [modalVisible, setModalVisible] = useState(false);
    const [editingUser, setEditingUser] = useState(null);
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const { users, loading: usersLoading, refetch } = useUsers();
    const { branches } = useBranches();
    const { enterprises } = useEnterprises();

    const handleCreate = () => {
        setEditingUser(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (user) => {
        setEditingUser(user);
        form.setFieldsValue({
            ...user,
            roles: user.roles?.map(role => role.replace('ROLE_', '').toLowerCase())
        });
        setModalVisible(true);
    };

    const handleDelete = async (id) => {
        Modal.confirm({
            title: 'Удалить пользователя?',
            content: 'Это действие нельзя отменить.',
            okText: 'Удалить',
            okType: 'danger',
            cancelText: 'Отмена',
            onOk: async () => {
                try {
                    await userService.deleteUser(id);
                    message.success('Пользователь удален');
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
            const userData = {
                ...values,
                roles: values.roles.map(role => `ROLE_${role.toUpperCase()}`)
            };

            if (editingUser) {
                await userService.updateUser(editingUser.id, userData);
                message.success('Пользователь обновлен');
            } else {
                await userService.createUser(userData);
                message.success('Пользователь создан');
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
            title: 'Имя пользователя',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'Роли',
            dataIndex: 'roles',
            key: 'roles',
            render: (roles) => (
                <Space>
                    {roles?.map(role => (
                        <Tag key={role} color="blue">
                            {role.replace('ROLE_', '')}
                        </Tag>
                    ))}
                </Space>
            ),
        },
        {
            title: 'Филиал',
            dataIndex: ['branch', 'name'],
            key: 'branch',
        },
        {
            title: 'Предприятие',
            dataIndex: ['enterprise', 'name'],
            key: 'enterprise',
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

    const roleOptions = [
        { value: 'admin', label: 'Администратор' },
        { value: 'association_eng', label: 'Инженер объединения' },
        { value: 'enterprise_eng', label: 'Инженер предприятия' },
        { value: 'branch_eng', label: 'Инженер филиала' },
        { value: 'user', label: 'Пользователь' },
    ];

    return (
        <Card
            title="Пользователи"
            extra={
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={handleCreate}
                >
                    Добавить пользователя
                </Button>
            }
        >
            <Table
                columns={columns}
                dataSource={users}
                loading={usersLoading}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />

            <Modal
                title={editingUser ? 'Редактировать пользователя' : 'Новый пользователь'}
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
                        name="username"
                        label="Имя пользователя"
                        rules={[{ required: true, message: 'Введите имя пользователя!' }]}
                    >
                        <Input placeholder="Введите имя пользователя" />
                    </Form.Item>

                    {!editingUser && (
                        <Form.Item
                            name="password"
                            label="Пароль"
                            rules={[{ required: true, message: 'Введите пароль!' }]}
                        >
                            <Input.Password placeholder="Введите пароль" />
                        </Form.Item>
                    )}

                    <Form.Item
                        name="roles"
                        label="Роли"
                        rules={[{ required: true, message: 'Выберите роли!' }]}
                    >
                        <Select
                            mode="multiple"
                            placeholder="Выберите роли"
                            options={roleOptions}
                        />
                    </Form.Item>

                    <Form.Item
                        name="branchId"
                        label="Филиал"
                    >
                        <Select
                            placeholder="Выберите филиал"
                            allowClear
                        >
                            {branches.map(branch => (
                                <Option key={branch.id} value={branch.id}>
                                    {branch.name}
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="enterpriseId"
                        label="Предприятие"
                    >
                        <Select
                            placeholder="Выберите предприятие"
                            allowClear
                        >
                            {enterprises.map(enterprise => (
                                <Option key={enterprise.id} value={enterprise.id}>
                                    {enterprise.name}
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button type="primary" htmlType="submit" loading={loading}>
                                {editingUser ? 'Обновить' : 'Создать'}
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

export default UsersList;