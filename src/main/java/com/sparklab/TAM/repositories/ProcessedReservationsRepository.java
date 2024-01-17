package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.ProcessedReservations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessedReservationsRepository extends JpaRepository<ProcessedReservations,Long> {

}
