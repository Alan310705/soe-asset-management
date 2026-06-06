package vn.edu.hust.soict.soe.assetmanagement.stock.enums;

/**
 * Type of stock movement stored in {@code stock_transactions}: receipt, issue, or adjustment.
 */
public enum TransactionType {
    RECEIPT,     // Nhập kho
    ISSUE,       // Xuất kho
    ADJUSTMENT   // Điều chỉnh
}
