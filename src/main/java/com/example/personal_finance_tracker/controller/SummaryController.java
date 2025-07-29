package com.example.personal_finance_tracker.controller;

import com.example.personal_finance_tracker.entity.TransactionEntity;
import com.example.personal_finance_tracker.entity.UserEntity;
import com.example.personal_finance_tracker.repo.TransactionEntityRepository;
import com.example.personal_finance_tracker.service.SummaryExportService;
import com.example.personal_finance_tracker.service.TransactionsService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final TransactionsService transactionsService;

    private final SummaryExportService summaryExportService;
    private final TransactionEntityRepository transactionEntityRepository;

    public SummaryController(TransactionsService transactionsService, SummaryExportService excelExportService,
                             TransactionEntityRepository transactionEntityRepository) {
        this.transactionsService = transactionsService;
        this.summaryExportService = excelExportService;
        this.transactionEntityRepository = transactionEntityRepository;
    }

    @GetMapping("/excel")
    public HttpEntity<?> exportExcel(@RequestParam String month, @AuthenticationPrincipal UserEntity userEntity) {



        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_" + month + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(summaryExportService.exportExel(month, userEntity.getId()));
    }

    @GetMapping("/pdf")
    public HttpEntity<?> exportPdf(@RequestParam String month, @AuthenticationPrincipal UserEntity userEntity) {



        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_" + month + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(summaryExportService.exportPDF(month, userEntity.getId()));
    }

  /*  @GetMapping("/excel")
    public HttpEntity<?> exportExcel(
            @RequestParam String month,
            @AuthenticationPrincipal UserEntity userEntity) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        List<TransactionEntity> transactions = transactionEntityRepository.findByUserIdAndTransactionDateBetween(userEntity.getId(), start, end);
        ByteArrayInputStream in = summaryExportService.exportExel(transactions);
        InputStreamResource file = new InputStreamResource(in);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_" + month + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    @GetMapping("/pdf")
    public HttpEntity<?> exportPdf(
            @RequestParam String month,
            @AuthenticationPrincipal UserEntity userEntity) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        List<TransactionEntity> transactions = transactionEntityRepository.findByUserIdAndTransactionDateBetween(userEntity.getId(), start, end);


        ByteArrayInputStream pdf = summaryExportService.exportPDF(transactions);
        InputStreamResource file = new InputStreamResource(pdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_" + month + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }*/
}
