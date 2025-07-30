package com.example.personal_finance_tracker.controller;

import com.example.personal_finance_tracker.entity.UserEntity;
import com.example.personal_finance_tracker.service.SummaryExportService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryExportService summaryExportService;

    public SummaryController(SummaryExportService summaryExportService) {
        this.summaryExportService = summaryExportService;
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
}
