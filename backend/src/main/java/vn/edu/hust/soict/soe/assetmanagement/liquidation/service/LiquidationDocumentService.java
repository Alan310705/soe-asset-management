package vn.edu.hust.soict.soe.assetmanagement.liquidation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hust.soict.soe.assetmanagement.asset.entity.FixedAsset;
import vn.edu.hust.soict.soe.assetmanagement.asset.repository.FixedAssetRepository;
import vn.edu.hust.soict.soe.assetmanagement.common.LiquidationPdfData;
import vn.edu.hust.soict.soe.assetmanagement.common.PdfDocumentBuilder;
import vn.edu.hust.soict.soe.assetmanagement.liquidation.entity.LiquidationRequest;
import vn.edu.hust.soict.soe.assetmanagement.lookup.service.LookupService;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Generates the official liquidation document (Biên bản thanh lý TSCĐ — Mẫu 02-TSCĐ).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LiquidationDocumentService {

    private final FixedAssetRepository fixedAssetRepository;
    private final LookupService lookupService;

    public String generateDocument(LiquidationRequest request) {
        String documentRef = toDocumentRef(request.getRequestCode());
        log.info("Liquidation document reference assigned: {} for request {}",
                documentRef, request.getRequestCode());
        return documentRef;
    }

    public byte[] generatePdf(LiquidationRequest request) {
        String documentRef = request.getDocumentRef() != null
                ? request.getDocumentRef()
                : toDocumentRef(request.getRequestCode());

        FixedAsset asset = fixedAssetRepository.findById(request.getAssetId()).orElse(null);
        LocalDate documentDate = request.getCompletedAt() != null
                ? request.getCompletedAt().toLocalDate()
                : LocalDate.now();

        String unitName = unitName(request.getRequestingUnitId());
        String conclusion = buildConclusion(request);

        LiquidationPdfData data = new LiquidationPdfData(
                documentRef,
                documentDate,
                unitName,
                unitName,
                request.getRequestCode(),
                request.getManagerApprovedAt() != null
                        ? request.getManagerApprovedAt().toLocalDate()
                        : documentDate,
                request.getDirectorApprovedBy(),
                request.getManagerApprovedBy(),
                request.getInitiatedBy(),
                asset != null ? asset.getName() : null,
                asset != null ? asset.getAssetCode() : null,
                asset != null ? asset.getCountryOfOrigin() : null,
                null,
                asset != null && asset.getAcquisitionDate() != null
                        ? asset.getAcquisitionDate().getYear()
                        : null,
                asset != null ? asset.getAssetCode() : null,
                asset != null ? asset.getOriginalCost() : null,
                asset != null ? asset.getAccumulatedDepreciation() : null,
                asset != null ? asset.getNetBookValue() : request.getCurrentMarketValue(),
                PdfDocumentBuilder.disposalMethodLabel(request.getDisposalMethod()),
                conclusion
        );

        return PdfDocumentBuilder.buildLiquidation(data);
    }

    public boolean markDocumentSigned(LiquidationRequest request) {
        log.info("Liquidation document {} marked as signed for request {}",
                request.getDocumentRef(), request.getRequestCode());
        return true;
    }

    private String buildConclusion(LiquidationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ban thanh lý thống nhất thanh lý TSCĐ nêu trên");
        if (request.getDisposalMethod() != null) {
            sb.append(" theo hình thức ")
                    .append(PdfDocumentBuilder.disposalMethodLabel(request.getDisposalMethod()));
        }
        sb.append(". ");
        if (request.getJustification() != null && !request.getJustification().isBlank()) {
            sb.append("Lý do: ").append(request.getJustification()).append(". ");
        }
        if (request.getFinalDisposalValue() != null) {
            sb.append("Giá trị thu được: ")
                    .append(PdfDocumentBuilder.formatVnd(request.getFinalDisposalValue()))
                    .append(".");
        }
        return sb.toString().trim();
    }

    private String toDocumentRef(String requestCode) {
        return requestCode.replace("TL-", "BBTL-");
    }

    private String unitName(UUID unitId) {
        return lookupService.findUnit(unitId)
                .map(u -> u.getName())
                .orElse(unitId.toString());
    }
}
