import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, InputNumber, Select, Button, message } from 'antd';
import { liquidationApi } from '../../api/liquidationApi';
import { handoverApi } from '../../api/handoverApi';
import { lookupApi } from '../../api/lookupApi';
import { assetApi } from '../../api/assetApi';
import type { LookupItem } from '../../types/common.types';
import type { FixedAsset } from '../../types/asset.types';
import PageHeader from '../../components/PageHeader';
import { filterAssetsAvailableForWorkflow } from '../../utils/workflowAssetFilter';

export default function LiquidationFormPage() {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [units, setUnits] = useState<LookupItem[]>([]);
  const [assets, setAssets] = useState<FixedAsset[]>([]);

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
    const asset = assets.find((a) => a.id === assetId);
    if (asset) {
      form.setFieldValue('requestingUnitId', asset.managingUnitId);
    } else {
      form.setFieldValue('requestingUnitId', undefined);
    }
  };

  return (
    <>
      <PageHeader title="Tạo yêu cầu thanh lý" />
      <Form form={form} layout="vertical" style={{ maxWidth: 640 }} onFinish={async (v) => {
        try {
          const created = await liquidationApi.create(v);
          message.success('Tạo yêu cầu thành công.');
          navigate(`/liquidations/${created.id}`);
        } catch {
          message.error('Tạo yêu cầu thất bại.');
        }
      }}>
        <Form.Item name="assetId" label="Tài sản" rules={[{ required: true }]}>
          <Select
            showSearch
            optionFilterProp="label"
            options={assets.map((a) => ({ value: a.id, label: `${a.assetCode} - ${a.name}` }))}
            onChange={handleAssetChange}
          />
        </Form.Item>
        <Form.Item name="requestingUnitId" label="Đơn vị yêu cầu (tự động từ tài sản)" rules={[{ required: true }]}>
          <Select disabled options={units.map((u) => ({ value: u.id, label: u.name }))} placeholder="Chọn tài sản trước" />
        </Form.Item>
        <Form.Item name="justification" label="Lý do thanh lý" rules={[{ required: true }]}><Input.TextArea rows={3} /></Form.Item>
        <Form.Item name="assetCondition" label="Tình trạng tài sản" rules={[{ required: true }]}>
          <Select options={['GOOD', 'FAIR', 'POOR', 'UNUSABLE'].map((v) => ({ value: v, label: v }))} />
        </Form.Item>
        <Form.Item name="currentMarketValue" label="Giá trị thị trường ước tính"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
        <Form.Item name="disposalMethod" label="Phương thức xử lý" rules={[{ required: true }]}>
          <Select options={['AUCTION', 'DONATION', 'DESTRUCTION', 'SALE'].map((v) => ({ value: v, label: v }))} />
        </Form.Item>
        <Button type="primary" htmlType="submit">Lưu nháp</Button>
      </Form>
    </>
  );
}
