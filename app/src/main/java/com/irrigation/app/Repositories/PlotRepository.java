package com.irrigation.app.Repositories;
import com.irrigation.app.Entities.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlotRepository extends JpaRepository<Plot, Long> {


    @Query("SELECT p FROM Plot p JOIN FETCH p.timeSlots")
    List<Plot> findAllWithTimeSlots();
}
