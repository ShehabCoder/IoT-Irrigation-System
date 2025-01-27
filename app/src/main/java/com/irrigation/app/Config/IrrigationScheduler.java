package com.irrigation.app.Config;

import com.irrigation.app.Services.IrrigationProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IrrigationScheduler {

    private final IrrigationProducerService producerService;

    public IrrigationScheduler(IrrigationProducerService producerService) {
        this.producerService = producerService;
    }

    @Scheduled(cron = "0 * * * * ?") // Every minute

    public void scheduleIrrigationRequests() {
        producerService.sendIrrigationRequests();
    }

}
