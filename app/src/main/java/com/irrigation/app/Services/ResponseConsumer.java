package com.irrigation.app.Services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irrigation.app.DTOs.PlotDTO;
import com.irrigation.app.Services.PlotService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResponseConsumer {

    private final PlotService plotService;

    public ResponseConsumer(PlotService plotService) {
        this.plotService = plotService;
    }

    /**
     * Listens to the 'irrigation_responses' topic and updates the plot status.
     *
     * @param message JSON string containing the irrigation response details.
     */
    @KafkaListener(topics = "${kafka.topic.responses}", groupId = "response_group")
    public void consumeResponse(String message) {
        System.out.println("Received irrigation response: " + message);

        try {
            // Parse the JSON response message
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> response = mapper.readValue(message, new TypeReference<Map<String, String>>() {});

            // Extract data from the response
            Long plotId = Long.parseLong(response.get("plot_id"));
            boolean status = "success".equalsIgnoreCase(response.get("status"));

            System.out.println("Processing response for Plot ID: " + plotId);

            // Update the status of all time slots for the plot
            plotService.updateTimeSlotStatus(plotId, status);

            System.out.println("Plot ID: " + plotId + " time slots status updated to: " + status);
        } catch (Exception e) {
            System.err.println("Failed to process irrigation response: " + e.getMessage());
        }
    }
}
