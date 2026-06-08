-- ============================================================
-- V8__add_more_seed_data.sql
-- Description: Expand sample data for realistic system testing.
-- Adds 10 Fixed Assets and 10 Consumable Materials.
-- ============================================================

-- ============================================================
-- FIXED ASSETS (10 Samples)
-- ============================================================
INSERT INTO assets (
    id, asset_code, name, category_id, managing_unit_id,
    serial_number, manufacturer, original_cost, acquisition_date,
    funding_source, useful_life_years, salvage_value,
    depreciation_method, net_book_value, status, created_by
) VALUES 
    -- 1. Xe ô tô Toyota (VEHICLE) -> Ban Giám đốc (HQ)
    (gen_random_uuid(), 'TS-2024-002', 'Xe ô tô Toyota Camry 2.0G', 
    (SELECT id FROM asset_categories WHERE code = 'VEHICLE'), '00000000-0000-0000-0000-000000000001', 
    'VIN-TOYOTA-9988', 'Toyota', 1200000000.00, '2023-05-10', 'Ngân sách nhà nước', 8, 0, 'STRAIGHT_LINE', 1200000000.00, 'IN_USE', 'admin'),

    -- 2. Máy in Laser (IT) -> Hành chính - Kỹ thuật (PHKT)
    (gen_random_uuid(), 'TS-2024-003', 'Máy in laser Canon LBP 2900', 
    (SELECT id FROM asset_categories WHERE code = 'IT'), '00000000-0000-0000-0000-000000000002', 
    'SN-CANON-1122', 'Canon', 3500000.00, '2024-02-20', 'Vốn tự có', 3, 0, 'STRAIGHT_LINE', 3500000.00, 'IN_USE', 'admin'),

    -- 3. Tòa nhà văn phòng (BUILDING) -> Ban Giám đốc (HQ)
    (gen_random_uuid(), 'TS-2020-001', 'Tòa nhà trụ sở chính (5 tầng)', 
    (SELECT id FROM asset_categories WHERE code = 'BUILDING'), '00000000-0000-0000-0000-000000000001', 
    NULL, 'Tổng công ty Xây dựng', 15000000000.00, '2020-01-01', 'Ngân sách nhà nước', 25, 0, 'STRAIGHT_LINE', 15000000000.00, 'IN_USE', 'admin'),

    -- 4. Bàn họp gỗ (EQUIPMENT) -> Ban Giám đốc (HQ)
    (gen_random_uuid(), 'TS-2024-004', 'Bàn họp phòng Giám đốc 2.4m', 
    (SELECT id FROM asset_categories WHERE code = 'EQUIPMENT'), '00000000-0000-0000-0000-000000000001', 
    NULL, 'Hòa Phát', 12000000.00, '2024-01-05', 'Vốn tự có', 5, 0, 'STRAIGHT_LINE', 12000000.00, 'IN_USE', 'admin'),

    -- 5. Điều hòa Daikin (EQUIPMENT) -> Kế toán - Tài chính (PKTCN)
    (gen_random_uuid(), 'TS-2023-005', 'Điều hòa Daikin Inverter 18000 BTU', 
    (SELECT id FROM asset_categories WHERE code = 'EQUIPMENT'), '00000000-0000-0000-0000-000000000003', 
    'SN-DAIKIN-3344', 'Daikin', 18500000.00, '2023-06-15', 'Ngân sách nhà nước', 5, 0, 'STRAIGHT_LINE', 18500000.00, 'MAINTENANCE', 'admin'),

    -- 6. Máy phát điện (MACHINE) -> Hành chính - Kỹ thuật (PHKT)
    (gen_random_uuid(), 'TS-2022-006', 'Máy phát điện Cummins 50kVA', 
    (SELECT id FROM asset_categories WHERE code = 'MACHINE'), '00000000-0000-0000-0000-000000000002', 
    'SN-CUMMINS-5566', 'Cummins', 150000000.00, '2022-11-20', 'Ngân sách nhà nước', 10, 0, 'STRAIGHT_LINE', 150000000.00, 'IN_USE', 'admin'),

    -- 7. Xe tải Hyundai (VEHICLE) -> Phòng Kinh doanh (PKD)
    (gen_random_uuid(), 'TS-2021-007', 'Xe tải Hyundai Mighty 110SP 7 tấn', 
    (SELECT id FROM asset_categories WHERE code = 'VEHICLE'), '00000000-0000-0000-0000-000000000004', 
    'VIN-HYUNDAI-7788', 'Hyundai', 750000000.00, '2021-08-10', 'Vốn tự có', 8, 0, 'STRAIGHT_LINE', 750000000.00, 'IN_USE', 'admin'),

    -- 8. Máy chiếu Epson (IT) -> Phòng Kinh doanh (PKD)
    (gen_random_uuid(), 'TS-2024-008', 'Máy chiếu Epson EB-X06', 
    (SELECT id FROM asset_categories WHERE code = 'IT'), '00000000-0000-0000-0000-000000000004', 
    'SN-EPSON-9900', 'Epson', 11500000.00, '2024-03-01', 'Vốn tự có', 4, 0, 'STRAIGHT_LINE', 11500000.00, 'IDLE', 'admin'),

    -- 9. Tủ tài liệu sắt (EQUIPMENT) -> Kế toán - Tài chính (PKTCN)
    (gen_random_uuid(), 'TS-2024-009', 'Tủ tài liệu sắt 4 ngăn', 
    (SELECT id FROM asset_categories WHERE code = 'EQUIPMENT'), '00000000-0000-0000-0000-000000000003', 
    NULL, 'Hòa Phát', 3500000.00, '2024-01-10', 'Ngân sách nhà nước', 5, 0, 'STRAIGHT_LINE', 3500000.00, 'IN_USE', 'admin'),

    -- 10. Máy chủ Server (IT) -> Hành chính - Kỹ thuật (PHKT)
    (gen_random_uuid(), 'TS-2023-010', 'Máy chủ Dell PowerEdge R740', 
    (SELECT id FROM asset_categories WHERE code = 'IT'), '00000000-0000-0000-0000-000000000002', 
    'SN-DELL-SV-112', 'Dell', 120000000.00, '2023-09-05', 'Ngân sách nhà nước', 5, 0, 'STRAIGHT_LINE', 120000000.00, 'IN_USE', 'admin');


-- ============================================================
-- CONSUMABLE MATERIALS (10 Samples)
-- ============================================================
INSERT INTO materials (
    id, material_code, name, category_id,
    unit_of_measure, unit_price, minimum_stock, created_by
) VALUES 
    (gen_random_uuid(), 'VT-2024-002', 'Bút bi Thiên Long TL-027 (Xanh)', (SELECT id FROM material_categories WHERE code = 'OFFICE'), 'Hộp', 65000.00, 20.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-003', 'Kẹp bướm 15mm Deli', (SELECT id FROM material_categories WHERE code = 'OFFICE'), 'Hộp', 15000.00, 15.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-004', 'Mực in laser Canon 303', (SELECT id FROM material_categories WHERE code = 'SPARE'), 'Hộp', 1200000.00, 5.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-005', 'Xăng sinh học E5 RON 92', (SELECT id FROM material_categories WHERE code = 'FUEL'), 'Lít', 23500.00, 100.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-006', 'Dầu Diesel 0.05S', (SELECT id FROM material_categories WHERE code = 'FUEL'), 'Lít', 21000.00, 150.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-007', 'Nước tẩy rửa bồn cầu Vim 900ml', (SELECT id FROM material_categories WHERE code = 'CHEMICAL'), 'Chai', 35000.00, 30.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-008', 'Pin tiểu AA Energizer', (SELECT id FROM material_categories WHERE code = 'EQUIPMENT'), 'Vỉ', 45000.00, 50.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-009', 'Bóng đèn LED bulb Rạng Đông 12W', (SELECT id FROM material_categories WHERE code = 'SPARE'), 'Cái', 60000.00, 20.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-010', 'Băng dính trong 5cm', (SELECT id FROM material_categories WHERE code = 'OFFICE'), 'Cuộn', 12000.00, 40.000, 'admin'),
    (gen_random_uuid(), 'VT-2024-011', 'Khăn giấy rút watersilk 250 tờ', (SELECT id FROM material_categories WHERE code = 'OTHER'), 'Gói', 22000.00, 50.000, 'admin');