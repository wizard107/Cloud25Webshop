package com.example.webshop.api.controller;

import com.example.webshop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class MailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-invoice")
    public String sendInvoice(@RequestParam String email, @RequestParam String orderId, @RequestParam String invoicePath) {
        try {
            emailService.sendEmail(email, "Rechnung - " + orderId, "Hier ist Ihre Rechnung", invoicePath);
            return " Rechnung gesendet an " + email;
        } catch (Exception e) {
            return "Fehler beim Senden der Rechnung: " + e.getMessage();
        }
    }
}