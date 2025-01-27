package com.irrigation.app.Services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    /**
     * Sends an SMS to the specified recipient.
     *
     * @param to      The recipient's phone number.
     * @param message The message to send.
     */
    public void sendWhatsAppMessage(String to, String message) {
        try {
            Message.creator(
                    new PhoneNumber("whatsapp:" +to),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
            System.out.println("whatsapp sent to " + to + ": " + message);
        } catch (Exception e) {
            System.err.println("Failed to whatsapp SMS: " + e.getMessage());
        }
    }
}