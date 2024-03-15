package org.nap.diabuddy_companion_server.service.user;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    String storeCode(String email);

    boolean validateCode(String email, String inputCode);

    String generateVerificationCode();
}
