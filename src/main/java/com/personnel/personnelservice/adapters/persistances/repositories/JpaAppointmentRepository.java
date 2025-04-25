package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.UUID;

public interface JpaAppointmentRepository extends JpaBaseRepository<Appointment> {
    Page<Appointment> findByCabinetIdAndDateTimeBetween(UUID cabinetId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    boolean existsByCabinetIdAndDateTime(UUID cabinetId, LocalDateTime dateTime);
}
