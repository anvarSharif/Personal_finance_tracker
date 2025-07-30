package com.example.personal_finance_tracker.service;

import com.example.personal_finance_tracker.entity.TransactionEntity;
import com.example.personal_finance_tracker.repo.TransactionEntityRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SummaryExportService {

    private final TransactionEntityRepository transactionEntityRepository;

    public SummaryExportService(TransactionEntityRepository transactionEntityRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
    }
    public List<TransactionEntity> getUserTransactionsForMonth(Long userId, String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return transactionEntityRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
    }

    public InputStreamResource exportExel(String month, Long userId) {
        try (Workbook workbook = new XSSFWorkbook()) {
            List<TransactionEntity> transactions = getUserTransactionsForMonth(userId,month);
            Sheet sheet = workbook.createSheet("Transactions");
            Row header = sheet.createRow(0);
            String[] columns = {"Sana", "Kategoriya", "Turi", "Miqdor", "Tavsif"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }
            int rowIdx = 1;
            BigDecimal total = BigDecimal.ZERO;
            for (TransactionEntity tx : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(tx.getTransactionDate().toString());
                row.createCell(1).setCellValue(tx.getCategory());
                row.createCell(2).setCellValue(tx.getType().name());
                row.createCell(3).setCellValue(tx.getAmount().doubleValue());
                row.createCell(4).setCellValue(tx.getDescription());
                total = total.add(tx.getAmount());
            }

            Row totalRow = sheet.createRow(++rowIdx);
            totalRow.createCell(3).setCellValue("Umumiy summa:");
            totalRow.createCell(4).setCellValue(total.doubleValue());

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            return new InputStreamResource(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Excel fayl yaratishda xatolik", e);
        }
    }

    public InputStreamResource exportPDF(String month, Long userId) {
        List<TransactionEntity> transactions = getUserTransactionsForMonth(userId,month);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("Oylik Tranzaksiyalar Hisoboti", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100f);
            table.setWidths(new int[]{3, 3, 2, 2, 5});

            Stream.of("Sana", "Kategoriya", "Turi", "Miqdor", "Tavsif")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(Color.LIGHT_GRAY);
                        header.setPhrase(new Phrase(columnTitle, headFont));
                        table.addCell(header);
                    });

            BigDecimal total = BigDecimal.ZERO;

            for (TransactionEntity tx : transactions) {
                table.addCell(new Phrase(tx.getTransactionDate().toString(), bodyFont));
                table.addCell(new Phrase(tx.getCategory(), bodyFont));
                table.addCell(new Phrase(tx.getType().name(), bodyFont));
                table.addCell(new Phrase(tx.getAmount().toString(), bodyFont));
                table.addCell(new Phrase(tx.getDescription(), bodyFont));
                total = total.add(tx.getAmount());
            }

            PdfPCell empty = new PdfPCell(new Phrase(""));
            empty.setColspan(3);
            empty.setBorder(Rectangle.NO_BORDER);
            table.addCell(empty);
            table.addCell(new Phrase("Umumiy:", headFont));
            table.addCell(new Phrase(total.toString(), headFont));
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("PDF yaratishda xatolik", e);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
        return new InputStreamResource(byteArrayInputStream);
    }
}
