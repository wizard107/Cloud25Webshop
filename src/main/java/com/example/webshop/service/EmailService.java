package com.example.webshop.service;


import com.example.webshop.api.model.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        @Qualifier("emailTemplateEngine") TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendInvoiceEmail(List<OrderItem> order) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("cloudcomputing24.25@gmail.com");
        helper.setTo(order.get(1).getOrder().getUser().getEmail());
        helper.setSubject("Invoice for Order #" + order.get(1).getOrder().getId());

        Context context = new Context();
        context.setVariable("order", order);

        String htmlContent = templateEngine.process("invoice_template", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}

