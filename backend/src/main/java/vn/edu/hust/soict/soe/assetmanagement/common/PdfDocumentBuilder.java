package vn.edu.hust.soict.soe.assetmanagement.common;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Generates official Vietnamese workflow PDFs (handover & liquidation minutes).
 */
public final class PdfDocumentBuilder {

    public static final String DEFAULT_COMPANY_NAME =
            "CÔNG TY SE - QUA TAO";

    private static final float MARGIN = 48f;

    private PdfDocumentBuilder() {
    }

    public static byte[] buildHandover(HandoverPdfData data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, MARGIN, MARGIN, MARGIN, MARGIN);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font normal = font(11, false);
            Font bold = font(11, true);
            Font title = font(14, true);
            Font smallBold = font(10, true);
            Font motto = font(11, true);

            LocalDate date = data.documentDate() != null ? data.documentDate() : LocalDate.now();
            String company = orDefault(data.companyName(), DEFAULT_COMPANY_NAME);

            doc.add(buildHandoverHeader(company, data.documentRef(), date, normal, bold, motto));
            doc.add(spacer(6f));

            Paragraph mainTitle = new Paragraph("BIÊN BẢN BÀN GIAO TÀI SẢN", title);
            mainTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(mainTitle);
            doc.add(spacer(10f));

            doc.add(new Paragraph(
                    "Hôm nay, " + vietnameseDate(date) + ", tại " + company + " chúng tôi gồm có:",
                    normal));
            doc.add(spacer(6f));

            doc.add(partyBlock("NGƯỜI BÀN GIAO", data.handoverPersonName(),
                    data.handoverDepartment(), bold, normal));
            doc.add(spacer(4f));
            doc.add(partyBlock("NGƯỜI NHẬN BÀN GIAO", data.receiverPersonName(),
                    data.receiverDepartment(), bold, normal));
            doc.add(spacer(6f));

            doc.add(new Paragraph(
                    "Cùng tiến hành bàn giao tài sản với nội dung cụ thể như sau:",
                    normal));
            doc.add(spacer(6f));

            doc.add(buildHandoverAssetTable(data, smallBold, normal));
            doc.add(spacer(8f));

            if (data.reason() != null && !data.reason().isBlank()) {
                doc.add(new Paragraph("Lý do bàn giao: " + data.reason(), normal));
                doc.add(spacer(4f));
            }

            doc.add(new Paragraph(
                    "Người bàn giao cam đoan đã bàn giao đầy đủ các tài sản nêu trên cho người nhận bàn giao.",
                    normal));
            doc.add(spacer(4f));
            doc.add(new Paragraph(
                    "Hai bên đồng ý với nội dung bàn giao nêu trên. Biên bản được lập thành 03 bản có giá trị pháp lý như nhau, "
                            + "mỗi bên giữ 01 bản, 01 bản lưu hồ sơ.",
                    normal));
            doc.add(spacer(16f));

            doc.add(buildSignatureRow(
                    new String[]{"Bên giao", "Bên nhận", "Người quản lý"},
                    new String[]{data.handoverPersonName(), data.receiverPersonName(), data.managerName()},
                    bold, normal));

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tạo file PDF bàn giao", e);
        }
    }

    public static byte[] buildLiquidation(LiquidationPdfData data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, MARGIN, MARGIN, MARGIN, MARGIN);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font normal = font(11, false);
            Font bold = font(11, true);
            Font title = font(14, true);
            Font small = font(10, false);
            Font smallBold = font(10, true);

            LocalDate date = data.documentDate() != null ? data.documentDate() : LocalDate.now();

            doc.add(buildLiquidationHeader(data, date, normal, small, bold));
            doc.add(spacer(8f));

            Paragraph mainTitle = new Paragraph("BIÊN BẢN THANH LÝ TSCĐ", title);
            mainTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(mainTitle);
            doc.add(spacer(4f));

            Paragraph dateLine = new Paragraph(vietnameseDateCapitalized(date), normal);
            dateLine.setAlignment(Element.ALIGN_CENTER);
            doc.add(dateLine);
            doc.add(spacer(8f));

            doc.add(new Paragraph(
                    "Căn cứ Quyết định số: " + dotted(data.decisionRef())
                            + " ngày " + dottedDate(data.decisionDate())
                            + " về việc thanh lý tài sản cố định.",
                    normal));
            doc.add(spacer(8f));

            doc.add(new Paragraph("I. Ban thanh lý TSCĐ gồm:", bold));
            doc.add(spacer(4f));
            doc.add(committeeRow("Ông/Bà:", data.committeeHeadName(), "Trưởng ban", normal));
            doc.add(committeeRow("Ông/Bà:", data.committeeMember1Name(), "Ủy viên", normal));
            doc.add(committeeRow("Ông/Bà:", data.committeeMember2Name(), "Ủy viên", normal));
            doc.add(spacer(8f));

            doc.add(new Paragraph("II. Tiến hành thanh lý TSCĐ", bold));
            doc.add(spacer(4f));
            doc.add(fieldLine("Tên, ký mã hiệu, qui cách (cấp hạng) TSCĐ:",
                    joinParts(data.assetName(), data.assetCode()), normal));
            doc.add(fieldLine("Số hiệu TSCĐ:", data.assetCode(), normal));
            doc.add(fieldLine("Nước sản xuất (xây dựng):", data.countryOfOrigin(), normal));
            doc.add(fieldLine("Năm sản xuất:", formatYear(data.yearOfManufacture()), normal));
            doc.add(fieldLine(
                    "Năm đưa vào sử dụng:",
                    formatYear(data.yearInUse()) + "          Số thẻ TSCĐ: " + dotted(data.assetCardNo()),
                    normal));
            doc.add(fieldLine("Nguyên giá TSCĐ:", formatMoney(data.originalCost()), normal));
            doc.add(fieldLine(
                    "Giá trị hao mòn đã trích đến thời điểm thanh lý:",
                    formatMoney(data.accumulatedDepreciation()), normal));
            doc.add(fieldLine("Giá trị còn lại của TSCĐ:", formatMoney(data.netBookValue()), normal));
            if (data.disposalMethodLabel() != null && !data.disposalMethodLabel().isBlank()) {
                doc.add(fieldLine("Hình thức thanh lý:", data.disposalMethodLabel(), normal));
            }
            doc.add(spacer(8f));

            doc.add(new Paragraph("III. Kết luận của Ban thanh lý TSCĐ", bold));
            doc.add(spacer(4f));
            String conclusion = data.conclusion();
            if (conclusion == null || conclusion.isBlank()) {
                doc.add(new Paragraph(
                        "Ban thanh lý thống nhất thanh lý TSCĐ nêu trên theo đúng quy định hiện hành.",
                        normal));
            } else {
                doc.add(new Paragraph(conclusion, normal));
            }
            doc.add(spacer(20f));

            PdfPTable footer = new PdfPTable(1);
            footer.setWidthPercentage(45f);
            footer.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.addElement(new Paragraph(vietnameseDateCapitalized(date), normal));
            cell.addElement(spacer(24f));
            cell.addElement(new Paragraph("Trưởng Ban thanh lý", bold));
            cell.addElement(new Paragraph("(Ký, họ tên)", small));
            cell.addElement(spacer(30f));
            if (data.committeeHeadName() != null && !data.committeeHeadName().isBlank()) {
                cell.addElement(new Paragraph(data.committeeHeadName(), smallBold));
            }
            footer.addCell(cell);
            doc.add(footer);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tạo file PDF thanh lý", e);
        }
    }

    public static String nullSafe(Object value) {
        return value != null ? value.toString() : "";
    }

    public static String formatVnd(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(amount) + " đồng";
    }

    public static String disposalMethodLabel(String method) {
        if (method == null) {
            return "";
        }
        return switch (method) {
            case "AUCTION" -> "Đấu giá";
            case "SCRAP" -> "Thanh lý phế liệu";
            case "DONATION" -> "Hiến tặng";
            default -> method;
        };
    }

    private static PdfPTable buildHandoverHeader(String company, String documentRef,
                                                  LocalDate date, Font normal, Font bold, Font motto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[]{52f, 48f});
        } catch (Exception ignored) {
        }

        PdfPCell left = borderlessCell();
        left.addElement(new Paragraph(company.toUpperCase(), bold));
        left.addElement(new Paragraph("Số: " + dotted(documentRef) + "/BBBG", normal));

        PdfPCell right = borderlessCell();
        right.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph motto1 = new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", motto);
        motto1.setAlignment(Element.ALIGN_CENTER);
        Paragraph motto2 = new Paragraph("Độc lập – Tự do – Hạnh phúc", bold);
        motto2.setAlignment(Element.ALIGN_CENTER);
        Paragraph mottoDate = new Paragraph("........., " + vietnameseDate(date), normal);
        mottoDate.setAlignment(Element.ALIGN_CENTER);
        right.addElement(motto1);
        right.addElement(motto2);
        right.addElement(mottoDate);

        table.addCell(left);
        table.addCell(right);
        return table;
    }

    private static PdfPTable buildLiquidationHeader(LiquidationPdfData data, LocalDate date,
                                                     Font normal, Font small, Font bold) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[]{55f, 45f});
        } catch (Exception ignored) {
        }

        PdfPCell left = borderlessCell();
        left.addElement(new Paragraph("Đơn vị: " + dotted(data.unitName()), normal));
        left.addElement(new Paragraph("Bộ phận: " + dotted(data.departmentName()), normal));

        PdfPCell right = borderlessCell();
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph formNo = new Paragraph("Mẫu số 02-TSCĐ", bold);
        formNo.setAlignment(Element.ALIGN_RIGHT);
        Paragraph circular = new Paragraph(
                "(Ban hành theo Thông tư số 200/2014/TT-BTC\nNgày 22/12/2014 của Bộ Tài chính)",
                small);
        circular.setAlignment(Element.ALIGN_RIGHT);
        right.addElement(formNo);
        right.addElement(circular);
        right.addElement(new Paragraph("Số: " + dotted(data.documentRef()), normal));
        right.addElement(new Paragraph("Nợ: ....................", normal));
        right.addElement(new Paragraph("Có: ....................", normal));

        table.addCell(left);
        table.addCell(right);
        return table;
    }

    private static PdfPTable buildHandoverAssetTable(HandoverPdfData data, Font headerFont, Font bodyFont) {
        PdfPTable table = new PdfPTable(new float[]{8f, 24f, 28f, 10f, 10f, 20f});
        table.setWidthPercentage(100);

        String[] headers = {
                "Stt", "Tên tài sản", "Thông số kỹ thuật, Mã thiết bị", "Đơn vị", "Số lượng", "Hiện trạng"
        };
        for (String header : headers) {
            table.addCell(headerCell(header, headerFont));
        }

        table.addCell(bodyCell("1", bodyFont));
        table.addCell(bodyCell(orDefault(data.assetName(), ""), bodyFont));
        table.addCell(bodyCell(orDefault(data.assetSpecs(), ""), bodyFont));
        table.addCell(bodyCell("Cái", bodyFont));
        table.addCell(bodyCell("1", bodyFont));
        table.addCell(bodyCell(orDefault(data.assetCondition(), ""), bodyFont));

        for (int i = 2; i <= 6; i++) {
            table.addCell(bodyCell(String.valueOf(i), bodyFont));
            for (int j = 0; j < 5; j++) {
                table.addCell(bodyCell("", bodyFont));
            }
        }
        return table;
    }

    private static Paragraph partyBlock(String title, String personName, String department,
                                        Font bold, Font normal) {
        Paragraph block = new Paragraph();
        block.add(new Phrase(title, bold));
        block.add(new Phrase("\nÔng/bà: " + dotted(personName), normal));
        block.add(new Phrase("\nChức vụ: ........................", normal));
        block.add(new Phrase("\nMSNV: ........................", normal));
        block.add(new Phrase("\nBộ phận: " + dotted(department), normal));
        return block;
    }

    private static Paragraph committeeRow(String label, String name, String role, Font normal) {
        return new Paragraph(
                label + " " + dotted(name)
                        + "          Chức vụ: ........................"
                        + "          Đại diện: ........................"
                        + "          " + role,
                normal);
    }

    private static PdfPTable buildSignatureRow(String[] titles, String[] names,
                                                Font bold, Font normal) {
        PdfPTable table = new PdfPTable(titles.length);
        table.setWidthPercentage(100);
        for (int i = 0; i < titles.length; i++) {
            PdfPCell cell = borderlessCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph title = new Paragraph(titles[i], bold);
            title.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(title);
            cell.addElement(spacer(28f));
            if (names[i] != null && !names[i].isBlank()) {
                Paragraph name = new Paragraph(names[i], normal);
                name.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(name);
            }
            table.addCell(cell);
        }
        return table;
    }

    private static Paragraph fieldLine(String label, String value, Font font) {
        return new Paragraph(label + " " + dotted(value), font);
    }

    private static PdfPCell headerCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4f);
        return cell;
    }

    private static PdfPCell bodyCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(4f);
        cell.setMinimumHeight(18f);
        return cell;
    }

    private static PdfPCell borderlessCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static Paragraph spacer(float height) {
        Paragraph p = new Paragraph(" ");
        p.setLeading(height);
        return p;
    }

    private static Font font(float size, boolean bold) {
        try {
            String resource = bold ? "/fonts/Arial-Bold.ttf" : "/fonts/Arial.ttf";
            try (InputStream is = PdfDocumentBuilder.class.getResourceAsStream(resource)) {
                if (is != null) {
                    byte[] bytes = is.readAllBytes();
                    String fontName = bold ? "Arial-Bold.ttf" : "Arial.ttf";
                    BaseFont baseFont = BaseFont.createFont(
                            fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, bytes, null);
                    return new Font(baseFont, size, bold ? Font.BOLD : Font.NORMAL);
                }
            }
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            return new Font(baseFont, size, bold ? Font.BOLD : Font.NORMAL);
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tải font PDF", e);
        }
    }

    private static String vietnameseDate(LocalDate date) {
        return String.format("ngày %02d tháng %02d năm %d",
                date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    private static String vietnameseDateCapitalized(LocalDate date) {
        return String.format("Ngày %02d tháng %02d năm %d",
                date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    private static String dotted(String value) {
        if (value == null || value.isBlank()) {
            return "........................";
        }
        return value;
    }

    private static String dottedDate(LocalDate date) {
        if (date == null) {
            return ".... tháng .... năm ....";
        }
        return String.format("%02d tháng %02d năm %d",
                date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    private static String orDefault(String value, String defaultValue) {
        return value != null && !value.isBlank() ? value : defaultValue;
    }

    private static String joinParts(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part != null && !part.isBlank()) {
                if (!sb.isEmpty()) {
                    sb.append(" - ");
                }
                sb.append(part);
            }
        }
        return sb.toString();
    }

    private static String formatYear(Integer year) {
        return year != null ? year.toString() : "........................";
    }

    private static String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "........................";
        }
        return formatVnd(amount);
    }
}
