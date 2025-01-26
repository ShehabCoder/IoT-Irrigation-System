package com.irrigation.app.DTOs;



import lombok.Data;

@Data
public class TimeSlotDTO {
    private String startTime; // ISO format
    private String endTime; // ISO format
    private double waterVolume; // Liters

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(double waterVolume) {
        this.waterVolume = waterVolume;
    }
}
