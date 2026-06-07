package vn.edu.hust.soict.soe.assetmanagement.handover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hust.soict.soe.assetmanagement.asset.entity.FixedAsset;
import vn.edu.hust.soict.soe.assetmanagement.asset.repository.FixedAssetRepository;
import vn.edu.hust.soict.soe.assetmanagement.common.HandoverPdfData;
import vn.edu.hust.soict.soe.assetmanagement.common.PdfDocumentBuilder;
import vn.edu.hust.soict.soe.assetmanagement.handover.entity.HandoverRequest;
import vn.edu.hust.soict.soe.assetmanagement.lookup.service.LookupService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Generates the official handover document (Biên bản bàn giao tài sản) — HL-03.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HandoverDocumentService {

    private final FixedAssetRepository fixedAssetRepository;
    private final LookupService lookupService;

    public String generateDocument(HandoverRequest handoverRequest) {
        String documentRef = toDocumentRef(handoverRequest.getRequestCode());
        log.info("Handover document reference assigned: {} for request {}",
                documentRef, handoverRequest.getRequestCode());
        return documentRef;
    }

    public byte[] generatePdf(HandoverRequest handoverRequest) {
        String documentRef = handoverRequest.getDocumentRef() != null
                ? handoverRequest.getDocumentRef()
                : toDocumentRef(handoverRequest.getRequestCode());

        FixedAsset asset = fixedAssetRepository.findById(handoverRequest.getAssetId()).orElse(null);
        LocalDate documentDate = handoverRequest.getHandoverDate() != null
                ? handoverRequest.getHandoverDate()
                : (handoverRequest.getCompletedAt() != null
                        ? handoverRequest.getCompletedAt().toLocalDate()
                        : LocalDate.now());

        HandoverPdfData data = new HandoverPdfData(
                documentRef,
                documentDate,
                PdfDocumentBuilder.DEFAULT_COMPANY_NAME,
                handoverRequest.getInitiatedBy(),
                unitName(handoverRequest.getFromUnitId()),
                handoverRequest.getConfirmedBy(),
                unitName(handoverRequest.getToUnitId()),
                handoverRequest.getDeptApprovedBy(),
                asset != null ? asset.getName() : null,
                buildAssetSpecs(asset),
                handoverRequest.getAssetCondition(),
                handoverRequest.getReason()
        );

        return PdfDocumentBuilder.buildHandover(data);
    }

    public boolean markDocumentSigned(HandoverRequest handoverRequest) {
        log.info("Handover document {} marked as signed for request {}",
                handoverRequest.getDocumentRef(), handoverRequest.getRequestCode());
        return true;
    }

    private String buildAssetSpecs(FixedAsset asset) {
        if (asset == null) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        if (asset.getAssetCode() != null) {
            parts.add("Mã: " + asset.getAssetCode());
        }
        if (asset.getSerialNumber() != null && !asset.getSerialNumber().isBlank()) {
            parts.add("S/N: " + asset.getSerialNumber());
        }
        if (asset.getManufacturer() != null && !asset.getManufacturer().isBlank()) {
            parts.add(asset.getManufacturer());
        }
        if (asset.getModel() != null && !asset.getModel().isBlank()) {
            parts.add(asset.getModel());
        }
        if (asset.getTechnicalSpecs() != null && !asset.getTechnicalSpecs().isBlank()) {
            parts.add(asset.getTechnicalSpecs());
        }
        return parts.stream().collect(Collectors.joining("; "));
    }

    private String toDocumentRef(String requestCode) {
        return requestCode.replace("BG-", "BBGTS-");
    }

    private String unitName(UUID unitId) {
        return lookupService.findUnit(unitId)
                .map(u -> u.getName())
                .orElse(unitId.toString());
    }
}
