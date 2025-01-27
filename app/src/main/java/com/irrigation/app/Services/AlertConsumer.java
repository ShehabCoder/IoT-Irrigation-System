package com.irrigation.app.Services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irrigation.app.Services.TwilioService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlertConsumer {

    private final TwilioService twilioService;

    public AlertConsumer(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    /**
     * Listens to the 'alerts' Kafka topic and sends an SMS notification.
     *
     * @param message JSON string containing the alert details.
     */
    @KafkaListener(topics = "${kafka.topic.alerts}", groupId = "alert_group")
    public void consumeAlert(String message) {
        System.out.println("Received alert message: " + message);

        try {
            // Parse the alert message
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> alert = mapper.readValue(message, new TypeReference<Map<String, String>>() {});
            String plotId = alert.get("plot_id");
            String alertMessage = alert.get("message");

            // Prepare SMS message
            String whatsappMessage  = "ALERT: Plot ID " + plotId + "\nDetails: " + alertMessage;

            // Send SMS to the intended recipient
            twilioService.sendWhatsAppMessage("+201141310002", whatsappMessage ); // Replace with the recipient's phone number
        } catch (Exception e) {
            System.err.println("Failed to process alert message: " + e.getMessage());
        }
    }
}