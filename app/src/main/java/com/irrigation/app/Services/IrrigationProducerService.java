package com.irrigation.app.Services;

import com.irrigation.app.Entities.Plot;
import com.irrigation.app.Entities.TimeSlot;
import com.irrigation.app.Repositories.PlotRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static java.time.LocalTime.parse;

@Service
public class IrrigationProducerService {

    private final PlotRepository plotRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public IrrigationProducerService(PlotRepository plotRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.plotRepository = plotRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendIrrigationRequests() {
        List<Plot> plots = plotRepository.findAllWithTimeSlots();

        if (plots.isEmpty()) {
            System.out.println("No plots found for irrigation.");
            return;
        }

        for (Plot plot : plots) {
            System.out.println("Processing Plot ID: " + plot.getId());

            for (TimeSlot timeSlot : plot.getTimeSlots()) {
                System.out.println("Processing TimeSlot: " + timeSlot.getStartTime());



                if (LocalTime.now().isAfter(timeSlot.getStartTime())
                        && LocalTime.now().isBefore(timeSlot.getEndTime())
                        && !timeSlot.isStatus()) {
                    String message = String.format("Plot ID: %d, Start Time: %s, End Time: %s, Water Volume: %.2f",
                            plot.getId(),
                            timeSlot.getStartTime(),
                            timeSlot.getEndTime(),
                            timeSlot.getWaterAmount());
                    kafkaTemplate.send("irrigation_requests", message);
                    System.out.println("Sent to irrigation_requests: " + message);
                }

            }
        }
    }



}
