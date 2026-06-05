package com.exam.utility.service;

import java.math.BigDecimal;
import java.util.Map;

public interface EmailService {
    void sendVerificationEmail(String to, String name, String token);
    void sendOtpEmail(String to, String name, String otp, String purpose);
    void sendPasswordResetEmail(String to, String name, String token, String otp);
    void sendWelcomeEmail(String to, String name);
    void sendBillGeneratedEmail(String to, String name, String billNumber, BigDecimal amount, String dueDate);
    void sendBillApprovedEmail(String to, String name, String billNumber, BigDecimal amount);
    void sendPaymentConfirmationEmail(String to, String name, String receiptNumber, BigDecimal amount, String billNumber);
    void sendOverdueBillEmail(String to, String name, String billNumber, BigDecimal amount);
    void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);
}
