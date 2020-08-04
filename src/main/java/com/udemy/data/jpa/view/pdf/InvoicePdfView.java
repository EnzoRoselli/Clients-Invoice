package com.udemy.data.jpa.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.udemy.data.jpa.models.entities.Invoice;
import com.udemy.data.jpa.models.entities.InvoiceItem;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@Component("invoice/ver")
public class InvoicePdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Invoice invoice = (Invoice) map.get("invoice");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase("Datos del Cliente"));
        cell.setBackgroundColor(new Color(184,218,255));
        cell.setPadding(8f);

        table.addCell(cell);
        table.addCell(invoice.getClient().getName() + " " + invoice.getClient().getSurname());
        table.addCell(invoice.getClient().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase("Datos de la Factura"));
        cell.setBackgroundColor(new Color(195,230,203));
        cell.setPadding(8f);

        table.addCell(cell);
        table.addCell("Folio: " + invoice.getId());
        table.addCell("Descripcion: " + invoice.getDescription());
        table.addCell("Fecha: " + invoice.getCreateAt());

        document.add(table);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float[] {3.5f, 1,1,1});
        table3.addCell("Producto");
        table3.addCell("Precio");
        table3.addCell("Cantidad");
        table3.addCell("Total");

        for (InvoiceItem item : invoice.getInvoiceItems()) {
            table3.addCell(item.getProduct().getName());
            table3.addCell(item.getProduct().getPrice().toString());

            cell = new PdfPCell(new Phrase(item.getQuantity().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table3.addCell(cell);
            table3.addCell(item.calculateImport().toString());
        }

        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);
        table3.addCell(invoice.getTotal().toString());

        document.add(table3);
    }
}
