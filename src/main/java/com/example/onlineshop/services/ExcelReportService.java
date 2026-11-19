package com.example.onlineshop.services;

import com.example.onlineshop.dto.AllOrdersResponsDto;
import com.example.onlineshop.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ExcelReportService {

    private final OrderService orderService;

    public byte[] generateOrdersExcel(int year, Integer month) {

        List<AllOrdersResponsDto> orders = orderService.getOrders(year, month);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Orders Report");

            // Header
            Row header = sheet.createRow(0);
            String[] columns = {
                    "Order ID", "User ID", "Order Date", "Status", "Total Amount",
                    "Shipping Address", "Payment Method", "Item Name",
                    "Quantity", "Price", "Description"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIndex = 1;

            for (AllOrdersResponsDto order : orders) {

                List<OrderItemDto> items = order.getItems();
                if (items == null) continue;

                for (OrderItemDto item : items) {

                    Row row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(order.getId() != null ? order.getId() : 0);

                    row.createCell(1).setCellValue(
                            order.getUserId() != null ? order.getUserId() : 0
                    );

                    row.createCell(2).setCellValue(
                            order.getOrderDate() != null ? order.getOrderDate().toString() : ""
                    );

                    row.createCell(3).setCellValue(
                            order.getStatus() != null ? order.getStatus() : ""
                    );

                    row.createCell(4).setCellValue(
                            order.getTotalAmount() != null
                                    ? order.getTotalAmount().doubleValue()
                                    : 0
                    );

                    row.createCell(5).setCellValue(
                            order.getShippingAddress() != null ? order.getShippingAddress() : ""
                    );

                    row.createCell(6).setCellValue(
                            order.getPaymentMethod() != null ? order.getPaymentMethod() : ""
                    );

                    // ITEMS
                    row.createCell(7).setCellValue(
                            item.getProductName() != null ? item.getProductName() : ""
                    );
                    row.createCell(8).setCellValue(item.getQuantity() != null ? item.getQuantity() : 0);
                    row.createCell(9).setCellValue(item.getPriceAtOrder());
                    row.createCell(10).setCellValue(
                            item.getProductDescription() != null ? item.getProductDescription() : ""
                    );
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }
}