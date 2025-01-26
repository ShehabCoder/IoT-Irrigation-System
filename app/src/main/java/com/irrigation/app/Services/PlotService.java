//package com.irrigation.app.Services;
//
//import com.irrigation.app.Entities.Plot;
//import com.irrigation.app.Repositories.PlotRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PlotService {
//    private final PlotRepository;
//
//    public PlotService(PlotRepository plotRepository) {
//        this.plotRepository = plotRepository;
//    }
//
//    public Plot addPlot(Plot plot) {
//        return plotRepository.save(plot);
//    }
//
//    public Plot updatePlot(Long id, Plot updatedPlot) {
//        return plotRepository.findById(id)
//                .map(plot -> {
//                    plot.setName(updatedPlot.getName());
//                    plot.setArea(updatedPlot.getArea());
//                    plot.setCropType(updatedPlot.getCropType());
//                    return plotRepository.save(plot);
//                })
//                .orElseThrow(() -> new RuntimeException("Plot not found"));
//    }
//
//    public List<Plot> getAllPlots() {
//        return plotRepository.findAll();
//    }
//    public  Plot getPlotById(Long id) {
//        return plotRepository.findById(id).orElse(null);
//    }
//}

package com.irrigation.app.Services;

import com.irrigation.app.DTOs.PlotDTO;
import com.irrigation.app.DTOs.TimeSlotDTO;
import com.irrigation.app.Entities.Plot;
import com.irrigation.app.Entities.TimeSlot;
import com.irrigation.app.Repositories.PlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlotService {

    private final PlotRepository plotRepository;

    public PlotService(PlotRepository plotRepository) {
        this.plotRepository = plotRepository;
    }

    public PlotDTO addPlot(PlotDTO plotDTO) {
        Plot plot = new Plot();
        plot.setName(plotDTO.getName());
        plot.setArea(plotDTO.getArea());
        plot.setCropType(plotDTO.getCropType());
        Plot savedPlot = plotRepository.save(plot);

        return mapToDTO(savedPlot);
    }

    public PlotDTO updatePlot(Long id, PlotDTO plotDTO) {
        Plot plot = plotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plot not found"));

        plot.setName(plotDTO.getName());
        plot.setArea(plotDTO.getArea());
        plot.setCropType(plotDTO.getCropType());
        Plot updatedPlot = plotRepository.save(plot);

        return mapToDTO(updatedPlot);
    }

    public List<PlotDTO> getAllPlots() {
        return plotRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PlotDTO getPlotById(Long id) {
        Plot plot = plotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plot not found"));

        return mapToDTO(plot);
    }

    public PlotDTO configurePlot(Long plotId, List<TimeSlotDTO> timeSlotDTOs) {
        // Find the plot by ID or throw an exception
        Plot plot = plotRepository.findById(plotId)
                .orElseThrow(() -> new RuntimeException("Plot not found"));

        // Create new time slots
        List<TimeSlot> newTimeSlots = timeSlotDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        // Set the plot relationship for each new time slot
        newTimeSlots.forEach(timeSlot -> timeSlot.setPlot(plot));

        // Replace the time slots by resetting the collection
        plot.getTimeSlots().clear(); // Ensure this maintains the entity reference
        plot.getTimeSlots().addAll(newTimeSlots);

        // Save the updated plot
        Plot updatedPlot = plotRepository.save(plot);

        // Return the updated PlotDTO
        return mapToDTO(updatedPlot);
    }

    private PlotDTO mapToDTO(Plot plot) {
        PlotDTO plotDTO = new PlotDTO();
        plotDTO.setId(plot.getId());
        plotDTO.setName(plot.getName());
        plotDTO.setArea(plot.getArea());
        plotDTO.setCropType(plot.getCropType());

        if (plot.getTimeSlots() != null) {
            List<TimeSlotDTO> timeSlotDTOs = plot.getTimeSlots().stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());

            // Reverse the list of timeSlotDTOs
            Collections.reverse(timeSlotDTOs);

            // Set the reversed list of TimeSlotDTOs
            plotDTO.setTimeSlots(timeSlotDTOs);
        }

        return plotDTO;
    }

    private TimeSlotDTO mapToDTO(TimeSlot timeSlot) {
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        timeSlotDTO.setStartTime(timeSlot.getStartTime().toString());
        timeSlotDTO.setEndTime(timeSlot.getEndTime().toString());
        timeSlotDTO.setWaterVolume(timeSlot.getWaterAmount());
        return timeSlotDTO;
    }

    private TimeSlot mapToEntity(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(LocalTime.parse(timeSlotDTO.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotDTO.getEndTime()));
        timeSlot.setWaterAmount(timeSlotDTO.getWaterVolume());
        return timeSlot;
    }

    public void deletePlot(Long plotId) {
        plotRepository.deleteById(plotId);
    }
}
