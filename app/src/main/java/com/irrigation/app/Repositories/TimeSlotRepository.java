package com.irrigation.app.Repositories;
import com.irrigation.app.Entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // Find all time slots by plot ID
    List<TimeSlot> findAllByPlotId(Long plotId);

    // Update the status of all time slots for a specific plot ID
    @Modifying
    @Transactional
    @Query("UPDATE TimeSlot t SET t.status = :status WHERE t.plot.id = :plotId")
    void updateStatusByPlotId(Long plotId, boolean status);
}
