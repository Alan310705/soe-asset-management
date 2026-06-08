import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Select, Button, message } from 'antd';
import { handoverApi } from '../../api/handoverApi';
import { liquidationApi } from '../../api/liquidationApi';
import { lookupApi } from '../../api/lookupApi';
import { assetApi } from '../../api/assetApi';
import type { LookupItem } from '../../types/common.types';
import type { FixedAsset } from '../../types/asset.types';
import PageHeader from '../../components/PageHeader';
import { filterAssetsAvailableForWorkflow } from '../../utils/workflowAssetFilter';

export default function HandoverFormPage() {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [units, setUnits] = useState<LookupItem[]>([]);
  const [assets, setAssets] = useState<FixedAsset[]>([]);
  const [selectedAsset, setSelectedAsset] = useState<FixedAsset | null>(null);

  useEffect(() => {
    Promise.all([
      lookupApi.managingUnits(),
      assetApi.list({ page: 0, size: 100 }),
      handoverApi.list(0, 500),
      liquidationApi.list(0, 500),
    ]).then(([unitList, assetPage, handovers, liquidations]) => {
      setUnits(unitList);
      setAssets(filterAssetsAvailableForWorkflow(
        assetPage.content,
        handovers.content,
        liquidations.content,
      ));
    });
  }, []);

  const handleAssetChange = (assetId: string) => {
    const asset = assets.find(a => a.id === assetId);
    setSelectedAsset(asset || null);
    if (asset) {
      form.setFieldValue('fromUnitId', asset.managingUnitId);
    }
  };

  return (
    <>
      <PageHeader title="Tạo yêu cầu bàn giao" />
      <Form form={form} layout="vertical" style={{ maxWidth: 640 }} onFinish={async (v) => {
        try {
          const created = await handoverApi.create(v);
          message.success('Tạo yêu cầu thành công.');
          navigate(`/handovers/${created.id}`);
        } catch { message.error('Tạo yêu cầu thất bại.'); }
      }}>
        <Form.Item name="assetId" label="Tài sản" rules={[{ required: true }]}>
          <Select options={assets.map(a => ({ value: a.id, label: `${a.assetCode} - ${a.name}` }))} showSearch optionFilterProp="label" onChange={handleAssetChange} />
        </Form.Item>
        <Form.Item name="fromUnitId" label="Đơn vị bàn giao (tự động từ tài sản)" rules={[{ required: true }]}>
          <Select disabled options={units.map(u => ({ value: u.id, label: u.name }))} placeholder="Chọn tài sản trước" />
        </Form.Item>
        <Form.Item name="toUnitId" label="Đơn vị tiếp nhận" rules={[{ required: true }]}>
          <Select options={units.map(u => ({ value: u.id, label: u.name }))} />
        </Form.Item>
        <Form.Item name="reason" label="Lý do" rules={[{ required: true }]}><Input.TextArea rows={3} /></Form.Item>
        <Form.Item name="handoverDate" label="Ngày bàn giao"><Input type="date" /></Form.Item>
        <Form.Item name="assetCondition" label="Tình trạng">
          <Select options={['GOOD', 'FAIR', 'POOR'].map(v => ({ value: v, label: v }))} allowClear />
        </Form.Item>
        <Button type="primary" htmlType="submit">Lưu nháp</Button>
      </Form>
    </>
  );
}
