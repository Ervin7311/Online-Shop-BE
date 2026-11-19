package com.example.onlineshop.controllers;

import com.example.onlineshop.services.ExcelReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orders")
public class ReportController {

    private final ExcelReportService excelReportService;

    public ReportController(ExcelReportService excelReportService) {
        this.excelReportService = excelReportService;
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportOrdersToExcel(
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {

        byte[] excel = excelReportService.generateOrdersExcel(year, month);

        if (excel == null || excel.length == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=orders.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
        }
    }

