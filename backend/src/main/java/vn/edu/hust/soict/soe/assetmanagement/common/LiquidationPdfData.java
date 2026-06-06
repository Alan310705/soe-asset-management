package vn.edu.hust.soict.soe.assetmanagement.common;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Data for generating the official fixed-asset liquidation minutes PDF (Mẫu 02-TSCĐ). */
public record LiquidationPdfData(
        String documentRef,
        LocalDate documentDate,
        String unitName,
        String departmentName,
        String decisionRef,
        LocalDate decisionDate,
        String committeeHeadName,
        String committeeMember1Name,
        String committeeMember2Name,
        String assetName,
        String assetCode,
        String countryOfOrigin,
        Integer yearOfManufacture,
        Integer yearInUse,
        String assetCardNo,
        BigDecimal originalCost,
        BigDecimal accumulatedDepreciation,
        BigDecimal netBookValue,
        String disposalMethodLabel,
        String conclusion
) {
}
