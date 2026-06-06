package vn.edu.hust.soict.soe.assetmanagement.common;

import java.time.LocalDate;

/** Data for generating the official handover minutes PDF (Biên bản bàn giao tài sản). */
public record HandoverPdfData(
        String documentRef,
        LocalDate documentDate,
        String companyName,
        String handoverPersonName,
        String handoverDepartment,
        String receiverPersonName,
        String receiverDepartment,
        String managerName,
        String assetName,
        String assetSpecs,
        String assetCondition,
        String reason
) {
}
