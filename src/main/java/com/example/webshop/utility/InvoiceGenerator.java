package com.example.webshop.utility;

import com.example.webshop.api.model.OrderItem;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class InvoiceGenerator {

    public static String generateInvoice(long orderId, String customerName, String customerEmail, List<OrderItem> items, double totalAmount) throws IOException {
        String filePath = "invoices/invoice_" + orderId + ".pdf";

        try {
            // Ensure directory exists
            new File("invoices").mkdirs();

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Add Title
            document.add(new Paragraph("Invoice"));

            // Add Customer Details
            document.add(new Paragraph("Order ID: " + orderId));
            document.add(new Paragraph("Customer: " + customerName));
            document.add(new Paragraph("Email: " + customerEmail));
            document.add(new Paragraph("\n")); // Leerzeile

            // Create a table for items
            float[] columnWidths = {250f, 100f, 100f}; // Adjusted column widths
            Table table = new Table(columnWidths);
            table.addCell(new Cell().add(new Paragraph("Item"))); //.setBold()));
            table.addCell(new Cell().add(new Paragraph("Quantity"))); //.setBold()));
            table.addCell(new Cell().add(new Paragraph("Price"))); //.setBold()));

            // Dynamically add order items
            for (OrderItem item : items) {
                table.addCell(new Cell().add(new Paragraph(item.getProduct().getName())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
                table.addCell(new Cell().add(new Paragraph("$" + String.format("%.2f", item.getProduct().getPrice()))));
            }

            document.add(table);
            document.add(new Paragraph("\nTotal Amount: $" + String.format("%.2f", totalAmount)));//.setBold());

            document.close();

            System.out.println("Invoice generated: " + filePath);
            return filePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Inner class for order items
    public static class Item {
        private String name;
        private int quantity;
        private double price;

        public Item(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}