//package com.irrigation.app.Controllers;
//import com.irrigation.app.Entities.Plot;
//import com.irrigation.app.Services.PlotService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/plots")
//public class PlotController {
//    private final PlotService;
//
//    public PlotController(PlotService plotService) {
//        this.plotService = plotService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Plot> addPlot(@RequestBody Plot plot) {
//
//        // Adding Plot
//
//        return ResponseEntity.ok(plotService.addPlot(plot));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Plot> updatePlot(@PathVariable Long id, @RequestBody Plot plot) {
//
//
//        return ResponseEntity.ok(plotService.updatePlot(id, plot));
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Plot>> getAllPlots() {
//        // use object mapper to print all plots
//
//
//        System.out.println("Getting all plots" );
//        return ResponseEntity.ok(plotService.getAllPlots());
//    }
//    @GetMapping("/get/{id}")
//    public ResponseEntity<Plot> getPlotById(@PathVariable Long id) {
//        return ResponseEntity.ok(plotService.getPlotById(id));
//    }
//}

package com.irrigation.app.Controllers;

import com.irrigation.app.DTOs.PlotDTO;
import com.irrigation.app.DTOs.TimeSlotDTO;
import com.irrigation.app.Services.PlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plots")
public class PlotController {

    private final PlotService plotService;

    public PlotController(PlotService plotService) {
        this.plotService = plotService;
    }

    @PostMapping
    public ResponseEntity<PlotDTO> addPlot(@RequestBody PlotDTO plotDTO) {
        validatePlotDTO(plotDTO);
        PlotDTO savedPlot = plotService.addPlot(plotDTO);
        return ResponseEntity.status(201).body(savedPlot);
    }

    @PutMapping("/{plotId}/configure")
    public ResponseEntity<PlotDTO> configurePlot(
            @PathVariable Long plotId,
            @RequestBody List<TimeSlotDTO> timeSlots) {
        validateTimeSlotDTOs(timeSlots);
        PlotDTO updatedPlot = plotService.configurePlot(plotId, timeSlots);
        return ResponseEntity.ok(updatedPlot);
    }


    @PutMapping("/{plotId}")
    public ResponseEntity<PlotDTO> updatePlot(
            @PathVariable Long plotId,
            @RequestBody PlotDTO plotDTO) {
        validatePlotDTO(plotDTO);
        PlotDTO updatedPlot = plotService.updatePlot(plotId, plotDTO);
        return ResponseEntity.ok(updatedPlot);
    }

    @GetMapping
    public ResponseEntity<List<PlotDTO>> getAllPlots() {
        List<PlotDTO> plots = plotService.getAllPlots();
        return ResponseEntity.ok(plots);
    }

    @GetMapping("/{plotId}")
    public ResponseEntity<PlotDTO> getPlotById(@PathVariable Long plotId) {
        PlotDTO plot = plotService.getPlotById(plotId);
        return ResponseEntity.ok(plot);
    }

    @DeleteMapping("/{plotId}")
    public ResponseEntity<Void> deletePlot(@PathVariable Long plotId) {
        plotService.deletePlot(plotId);
        return ResponseEntity.noContent().build();
    }

    private void validatePlotDTO(PlotDTO plotDTO) {
        if (plotDTO.getName() == null || plotDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Plot name cannot be null or blank");
        }
        if (plotDTO.getArea() <= 0) {
            throw new IllegalArgumentException("Plot area must be greater than 0");
        }
        if (plotDTO.getCropType() == null || plotDTO.getCropType().isBlank()) {
            throw new IllegalArgumentException("Crop type cannot be null or blank");
        }
    }

    private void validateTimeSlotDTOs(List<TimeSlotDTO> timeSlots) {
        if (timeSlots == null || timeSlots.isEmpty()) {
            throw new IllegalArgumentException("Time slots cannot be null or empty");
        }
        for (TimeSlotDTO timeSlot : timeSlots) {
            if (timeSlot.getStartTime() == null || timeSlot.getStartTime().isBlank()) {
                throw new IllegalArgumentException("Start time cannot be null or blank");
            }
            if (timeSlot.getEndTime() == null || timeSlot.getEndTime().isBlank()) {
                throw new IllegalArgumentException("End time cannot be null or blank");
            }
            if (timeSlot.getWaterVolume() <= 0) {
                throw new IllegalArgumentException("Water volume must be greater than 0");
            }
        }
    }
}
