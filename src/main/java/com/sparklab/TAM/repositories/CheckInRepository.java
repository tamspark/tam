package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn,Long> {
    boolean existsByReservationId(String reservationId);
    CheckIn findByReservationId(String reservationId);
}
