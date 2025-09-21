import React, { useState, useEffect } from 'react';
import { Form, Input, Select, Button, Card, Row, Col, Table, InputNumber, DatePicker, message } from 'antd';
import { useBranches } from '../../hooks/useBranches.js';
import { useWorkCategories } from '../../hooks/useWorkCategories.js';
import { usePlans } from '../../hooks/usePlans.js';
import { reportService } from '../../services/reportService';
import dayjs from 'dayjs';

const { Option } = Select;
const { MonthPicker } = DatePicker;

const ReportForm = ({ editMode, initialData, onSuccess }) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const { branches, loading: branchesLoading } = useBranches();
    const { workCategories } = useWorkCategories();
    const { plans } = usePlans();

    const [selectedBranch, setSelectedBranch] = useState(null);
    const [selectedPeriod, setSelectedPeriod] = useState(null);
    const [reportItems, setReportItems] = useState([]);
    const [quarterlyPlans, setQuarterlyPlans] = useState({});

    useEffect(() => {
        if (initialData) {
            form.setFieldsValue({
                ...initialData,
                period: dayjs(initialData.period)
            });
            setSelectedBranch(initialData.branchId);
            setSelectedPeriod(initialData.period);
            setReportItems(initialData.reportItems || []);
        }
    }, [initialData, form]);

    useEffect(() => {
        if (selectedBranch && selectedPeriod) {
            loadQuarterlyPlans();
        }
    }, [selectedBranch, selectedPeriod]);

    const loadQuarterlyPlans = async () => {
        try {
            const year = dayjs(selectedPeriod).year();
            const quarter = Math.floor(dayjs(selectedPeriod).month() / 3) + 1;

            const plan = plans.find(p =>
                p.branchId === selectedBranch && p.year === year
            );

            if (plan) {
                const quarterlyData = {};
                workCategories.forEach(category => {
                    const planItem = plan.planItems?.find(item => item.workCategoryId === category.id);
                    if (planItem) {
                        quarterlyData[category.id] = planItem[`q${quarter}Plan`] || 0;
                    }
                });
                setQuarterlyPlans(quarterlyData);
            }
        } catch (error) {
            console.error('Error loading quarterly plans:', error);
        }
    };

    const handleReportItemChange = (workCategoryId, value) => {
        const updatedItems = reportItems.filter(item => item.workCategoryId !== workCategoryId);

        if (value !== null && value !== undefined) {
            updatedItems.push({
                workCategoryId,
                actual: value
            });
        }

        setReportItems(updatedItems);
    };

    const onFinish = async (values) => {
        setLoading(true);
        try {
            const reportData = {
                ...values,
                period: values.period.format('YYYY-MM'),
                reportItems: reportItems.reduce((acc, item) => {
                    acc[item.workCategoryId] = item.actual;
                    return acc;
                }, {})
            };

            if (editMode && initialData?.id) {
                await reportService.updateReport(initialData.id, reportData);
                message.success('Отчет успешно обновлен!');
            } else {
                await reportService.createReport(reportData);
                message.success('Отчет успешно создан!');
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

    const renderReportTable = (categories, title) => (
        <Card title={title} size="small" style={{ marginBottom: 16 }}>
            <Table
                dataSource={categories}
                pagination={false}
                size="small"
                scroll={{ x: 800 }}
                columns={[
                    {
                        title: 'Наименование работ',
                        dataIndex: 'name',
                        key: 'name',
                        width: '30%',
                        fixed: 'left',
                    },
                    {
                        title: 'Годовой план',
                        key: 'annualPlan',
                        render: (_, record) => {
                            const plan = plans.find(p =>
                                p.branchId === selectedBranch &&
                                p.year === dayjs(selectedPeriod)?.year()
                            );
                            const planItem = plan?.planItems?.find(item => item.workCategoryId === record.id);
                            return planItem ? planItem.annualPlan : 0;
                        },
                    },
                    {
                        title: 'Квартальный план',
                        key: 'quarterlyPlan',
                        render: (_, record) => quarterlyPlans[record.id] || 0,
                    },
                    {
                        title: 'Факт за месяц',
                        key: 'actual',
                        render: (_, record) => (
                            <InputNumber
                                min={0}
                                placeholder="0"
                                value={reportItems.find(item => item.workCategoryId === record.id)?.actual}
                                onChange={(value) => handleReportItemChange(record.id, value)}
                                style={{ width: '100%' }}
                            />
                        ),
                    },
                    {
                        title: 'Выполнение %',
                        key: 'completion',
                        render: (_, record) => {
                            const actual = reportItems.find(item => item.workCategoryId === record.id)?.actual || 0;
                            const quarterlyPlan = quarterlyPlans[record.id] || 0;
                            const percentage = quarterlyPlan > 0 ? (actual / quarterlyPlan) * 100 : 0;

                            return (
                                <span style={{ color: percentage >= 100 ? '#52c41a' : percentage >= 80 ? '#faad14' : '#f5222d' }}>
                  {percentage.toFixed(1)}%
                </span>
                            );
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
        >
            <Row gutter={16}>
                <Col xs={24} md={8}>
                    <Form.Item
                        name="branchId"
                        label="Филиал"
                        rules={[{ required: true, message: 'Выберите филиал!' }]}
                    >
                        <Select
                            placeholder="Выберите филиал"
                            loading={branchesLoading}
                            onChange={setSelectedBranch}
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
                <Col xs={24} md={8}>
                    <Form.Item
                        name="period"
                        label="Период"
                        rules={[{ required: true, message: 'Выберите период!' }]}
                    >
                        <MonthPicker
                            placeholder="Выберите месяц и год"
                            format="MMMM YYYY"
                            style={{ width: '100%' }}
                            onChange={setSelectedPeriod}
                            disabled={editMode}
                        />
                    </Form.Item>
                </Col>
            </Row>

            {selectedBranch && selectedPeriod && (
                <>
                    {renderReportTable(mainNetworkCategories, 'Основная сеть')}
                    {renderReportTable(distributionNetworkCategories, 'Распределительная сеть')}
                </>
            )}

            <Form.Item>
                <Button
                    type="primary"
                    htmlType="submit"
                    loading={loading}
                    size="large"
                    disabled={!selectedBranch || !selectedPeriod}
                >
                    {editMode ? 'Обновить отчет' : 'Создать отчет'}
                </Button>
            </Form.Item>
        </Form>
    );
};

export default ReportForm;