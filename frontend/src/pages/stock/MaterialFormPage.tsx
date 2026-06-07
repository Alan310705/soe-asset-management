import { useEffect, useState } from 'react';
import { Form, Input, InputNumber, Select, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import { stockApi } from '../../api/stockApi';
import { lookupApi } from '../../api/lookupApi';
import type { LookupItem } from '../../types/common.types';
import PageHeader from '../../components/PageHeader';

export default function MaterialFormPage() {
  const navigate = useNavigate();
  const [categories, setCategories] = useState<LookupItem[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    lookupApi.materialCategories().then(setCategories);
  }, []);

  return (
    <>
      <PageHeader title="Thêm vật tư mới" />
      <Form layout="vertical" style={{ maxWidth: 640 }} onFinish={async (v) => {
        setLoading(true);
        try {
          await stockApi.createMaterial(v);
          message.success('Thêm vật tư thành công.');
          navigate('/materials');
        } catch {
          message.error('Thêm vật tư thất bại.');
        } finally {
          setLoading(false);
        }
      }}>
        <Form.Item name="materialCode" label="Mã vật tư" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="name" label="Tên vật tư" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="categoryId" label="Danh mục" rules={[{ required: true }]}>
          <Select options={categories.map(c => ({ value: Number(c.id), label: c.name }))} />
        </Form.Item>
        <Form.Item name="unitOfMeasure" label="Đơn vị tính" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="unitPrice" label="Đơn giá">
          <InputNumber min={0} style={{ width: '100%' }} />
        </Form.Item>
        <Form.Item name="minimumStock" label="Tồn tối thiểu">
          <InputNumber min={0} style={{ width: '100%' }} />
        </Form.Item>
        <Button type="primary" htmlType="submit" loading={loading}>Lưu</Button>
      </Form>
    </>
  );
}
