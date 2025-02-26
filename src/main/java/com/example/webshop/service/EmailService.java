package com.example.webshop.service;

import com.example.webshop.api.model.*;
import com.example.webshop.api.model.enums.OrderStatus;
import com.example.webshop.api.model.enums.PaymentMethod;
import com.example.webshop.api.model.enums.PaymentStatus;
import com.example.webshop.api.model.enums.ProductCategories;
import com.example.webshop.utility.InvoiceGenerator;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.activation.DataHandler;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class EmailService {
    private static final String APPLICATION_NAME = "WebshopMailer";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_FILE = "src/main/resources/mail_secret.json";
    private static final String SENDER_EMAIL = "webshop-low-tech@loyal-polymer-447814-a4.iam.gserviceaccount.com";


    private Gmail getGmailService() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // 🔹 Verwende `GoogleCredentials` statt `GoogleCredential`
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/gmail.send"));

        return new Gmail.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public void sendConfirmation(OrderDetails order, List<OrderItem> items) throws Exception {
        //create Invoice

        //create Mail
        String subject="Order Confirmation - #" +order.getId();
        String body = "<html><body>"
                + "<h2>Order Confirmation - #" + order.getId() + "</h2>"
                + "<p>Dear <strong>" + order.getUser().getName() + "</strong>,</p>"
                + "<p>Thank you for your order! Your order ID is <strong>#" + order.getId() + "</strong>.</p>"
                + "<p>We are processing your order and will notify you once it has been shipped.</p>"
                + "<hr>"
                + "<p><strong>Order Summary:</strong></p>"
                + "<p>Total Amount: <strong>$" + order.getTotalAmount() + "</strong></p>"
                + "<p>Shipping Address: " + order.getShippingAddress() + "</p>"
                + "<p>Estimated Delivery: <strong>3-5 business days</strong></p>"
                + "<hr>"
                + "<p>Attached is your invoice for this order.</p>"
                + "<p>If you have any questions, feel free to contact us.</p>"
                + "<p>Best regards, <br><strong>Webshop Team</strong></p>"
                + "</body></html>";
        String path=InvoiceGenerator.generateInvoice(order.getId(),order.getUser().getName(),order.getUser().getEmail(),items,order.getTotalAmount());
        sendEmail(order.getUser().getEmail(),subject,body,path);
    }

    public void sendEmail(String to, String subject, String body, String attachmentPath) throws Exception {
        Gmail service = getGmailService();

        // MIME Nachricht erstellen
        MimeMessage email = createEmailWithAttachment(to, SENDER_EMAIL, subject, body, attachmentPath);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawEmailBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawEmailBytes);

        // Gmail API E-Mail senden
        Message message = new Message();
        message.setRaw(encodedEmail);

        // 🔹 Replace "me" with the impersonated email (Fix Precondition Error)
        service.users().messages().send(SENDER_EMAIL, message).execute();

        System.out.println("✅ E-Mail erfolgreich gesendet an: " + to);
    }

    private MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, String attachmentPath) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Textteil
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(bodyText);

        // Anhang
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(new FileInputStream(attachmentPath), "application/pdf")));
        attachmentPart.setFileName("Invoice.pdf");

        // Multipart Nachricht
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        email.setContent(multipart);

        return email;
    }

    public static void main(String[] args) throws Exception {
        // Mock Order Details
        User user = new User((long)1,"John Doe", "houdael@outlook.de","test strasse");

        // Create mock order items

        // Create a mock order
        OrderDetails order = new OrderDetails(
                123456L,
                "2025-02-25",
                850.99,
                "USD",
                "123 Main St, New York, NY",
                user,
                OrderStatus.DELIVERED,
                PaymentStatus.PAID,
                PaymentMethod.CREDIT_CARD
        );
        Product product=new Product((long)4,"Tisch","Das ein Tisch",
                ProductCategories.BEDROOM_FURNITURE,new Inventory((long)4,3,
                "test","test@test.de","test","test"),
                12.12,"bild","farbe","material",12,12,12,12);
        OrderItem item1 = new OrderItem(2, order, product);
        OrderItem item2 = new OrderItem(3, order, product);
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);


        // Print to verify
        System.out.println("✅ Mock Order Created: " + order.getId());
        System.out.println("Customer: " + order.getUser().getName() + " - " + order.getUser().getEmail());
        System.out.println("Total Amount: $" + order.getTotalAmount());
        System.out.println("Payment Status: " + order.getPaymentStatus());
        // Send Order Confirmation Email with Invoice
        EmailService emailService = new EmailService();
        emailService.sendConfirmation(order, items);
    }
}