package com.example.webshop.service;
import com.example.webshop.api.model.Inventory;
import com.example.webshop.api.model.OrderDetails;
import com.example.webshop.api.model.OrderItem;
import com.example.webshop.utility.InvoiceGenerator;
import com.example.webshop.utility.MailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Konstruktor für manuelle Initialisierung
    public EmailService() {
        this.mailSender = createMailSender();
    }

    private JavaMailSender createMailSender() {
        MailConfig mailConfig=new MailConfig();
        JavaMailSender mailSender=mailConfig.javaMailSender();
        return mailSender;
    }

    @Async
    public void sendInvoice(String toEmail, OrderDetails order, List<OrderItem> items) throws Exception {
        String subject = "Invoice for Order " + order.getId();
        String text = "Dear " + order.getUser().getName() + ",\n\nPlease find attached your invoice for order ID: " + order.getId();

        // 🔹 Generate the invoice PDF using InvoiceGenerator
        String invoicePath = InvoiceGenerator.generateInvoice(order.getId(), order.getUser().getName(), order.getUser().getEmail(), items, order.getTotalAmount());
        if (invoicePath == null) {
            throw new Exception("Failed to generate invoice PDF.");
        }

        // 🔹 Convert the PDF file into a byte array for email attachment
        File invoiceFile = new File(invoicePath);
        byte[] pdfBytes = Files.readAllBytes(invoiceFile.toPath());

        // 🔹 Create email with PDF attachment
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("cloudcomputing24.25@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text);

        // 🔹 Attach the PDF invoice
        helper.addAttachment("Invoice_" + order.getId() + ".pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));

        // 🔹 Send the email
        mailSender.send(message);

        System.out.println("Invoice email sent to: " + toEmail);
    }
    public void sendShipmentConfirmationWithDelay(OrderDetails orderDetails) {
        scheduler.schedule(() -> {
            try {
                sendShipmentConfirmation(orderDetails);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.MINUTES);
    }

    @Async
    public void sendShipmentConfirmation(OrderDetails orderDetails) throws Exception {
        String subject = "📦 Your Order #" + orderDetails.getId() + " Has Shipped!";
        String text = "Dear " + orderDetails.getUser().getName() + ",\n\n"
                + "Your order has been shipped!\n"
                + "Tracking Number: " + orderDetails.getId() + "\n"
                + "Estimated Delivery Date: " + orderDetails.getOrderDate() + "\n\n"
                + "You can track your shipment using the following link:\n"
                + "https://tracking.example.com/" + orderDetails.getId() + "\n\n"
                + "Thank you for shopping with us!";

        // Create email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("cloudcomputing24.25@gmail.com");
        helper.setTo(orderDetails.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(text);

        // Send email
        mailSender.send(message);

        System.out.println("Shipment confirmation email sent to: " + orderDetails.getUser().getEmail());
    }

    void sendLowStockNotification(Inventory inventory) throws MessagingException, MessagingException {
        ProductService productService=new ProductService();
        String subject = "⚠️ Low Stock Alert: " + inventory.getId();
        String text = "The stock for product **" + inventory.getId() + "** is running low.\n\n"
                + "Current Stock: " + inventory.getStock() + "\n"
                + "Please restock as soon as possible!";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("cloudcomputing24.25@gmail.com");
        helper.setTo("cloudcomputing24.25@gmail.com");
        helper.setSubject(subject);
        helper.setText(text);

        // Send email
        mailSender.send(message);
        System.out.println("📧 Low stock notification sent for: " + inventory.getId());
    }
}