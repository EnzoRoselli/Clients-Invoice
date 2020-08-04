package com.udemy.data.jpa.view.xlsx;

import com.udemy.data.jpa.models.entities.Invoice;
import com.udemy.data.jpa.models.entities.InvoiceItem;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("invoice/ver.xlsx")
public class InvoiceXlsxView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook,
                                      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");

        Invoice invoice = (Invoice) map.get("invoice");
        Sheet sheet = workbook.createSheet("Factura Spring");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        cell.setCellValue("Datos del Cliente");

        row = sheet.createRow(1);
        cell = row.createCell(0);

        cell.setCellValue(invoice.getClient().getName() + " " + invoice.getClient().getSurname());

        row = sheet.createRow(2);
        cell = row.createCell(0);

        cell.setCellValue(invoice.getClient().getEmail());

        sheet.createRow(4).createCell(0).setCellValue("Datos de la Factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + invoice.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: " + invoice.getDescription());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + invoice.getCreateAt());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setFillBackgroundColor(IndexedColors.GOLD.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(headerStyle);
        header.getCell(1).setCellStyle(headerStyle);
        header.getCell(2).setCellStyle(headerStyle);
        header.getCell(3).setCellStyle(headerStyle);

        int rownum = 10;
        for (InvoiceItem item: invoice.getInvoiceItems()) {
            Row fila = sheet.createRow(rownum++);

            cell = fila.createCell(0);
            cell.setCellValue(item.getProduct().getName());
            cell.setCellStyle(bodyStyle);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProduct().getPrice());
            cell.setCellStyle(bodyStyle);

            cell = fila.createCell(2);
            cell.setCellValue(item.getQuantity());
            cell.setCellStyle(bodyStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calculateImport());
            cell.setCellStyle(bodyStyle);
        }

        Row filatotal = sheet.createRow(rownum);
        cell = filatotal.createCell(2);
        cell.setCellValue("Gran Total: ");

        cell = filatotal.createCell(3);
        cell.setCellValue(invoice.getTotal());
    }
}
