import { useEffect, useState } from 'react';
import { Table, message, Button } from 'antd';
import { Link } from 'react-router-dom';
import { stockApi, type Material } from '../../api/stockApi';
import PageHeader from '../../components/PageHeader';
import { formatCurrency } from '../../utils/formatCurrency';
import { ROLES, useHasAnyRole } from '../../utils/roleGuard';

export default function MaterialListPage() {
  const canCreate = useHasAnyRole([ROLES.WAREHOUSE]);
  const [data, setData] = useState<Material[]>([]);
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);

  const load = (p = 0) => {
    setLoading(true);
    stockApi.materials({ page: p, size: 20 })
      .then(r => { setData(r.content); setTotal(r.totalElements); setPage(p); })
      .catch(() => message.error('Không tải được danh sách vật tư.'))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(0); }, []);

  return (
    <>
      <PageHeader title="Danh mục vật tư" extra={canCreate && <Link to="/materials/new"><Button type="primary">Thêm vật tư</Button></Link>} />
      <Table rowKey="id" loading={loading} dataSource={data}
        pagination={{ current: page + 1, total, pageSize: 20, onChange: (p) => load(p - 1) }}
        columns={[
          { title: 'Mã VT', dataIndex: 'materialCode' },
          { title: 'Tên', dataIndex: 'name' },
          { title: 'Danh mục', dataIndex: 'categoryName' },
          { title: 'ĐVT', dataIndex: 'unitOfMeasure' },
          { title: 'Đơn giá', render: (_, r) => r.unitPrice != null ? formatCurrency(r.unitPrice) : '—' },
          { title: 'Tồn tối thiểu', dataIndex: 'minimumStock' },
        ]}
      />
    </>
  );
}
