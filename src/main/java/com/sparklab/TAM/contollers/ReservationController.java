package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.report.FilterDTO;
import com.sparklab.TAM.services.ReservationService;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("TAM/{userId}/reservations")
public class ReservationController {


    private final ReservationService reservationService;

    @GetMapping("/calendar")
    public ResponseEntity<?> getAllReservationsByDateCalendar(@PathVariable String userId,@RequestParam String fromDate,@RequestParam String toDate) {
        try {
            return new ResponseEntity<>(reservationService.getAllReservationsByDateCalendar(userId,fromDate,toDate), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @GetMapping("/calendar/{apartmentId}")
    public ResponseEntity<?> getAllReservationsByApartmentId(@PathVariable String userId, @PathVariable int apartmentId, @RequestParam String fromDate,@RequestParam String toDate) {
        try {
            return new ResponseEntity<>(reservationService.getAllReservationByApartmentId(userId,apartmentId,fromDate,toDate), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }


    @GetMapping("{id}")
    public ResponseEntity<?> getReservationById(@PathVariable String userId, @PathVariable String id) {
        try {
            return new ResponseEntity<>(reservationService.getReservationById(userId, id), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @GetMapping("filteredReservations")
    public ResponseEntity<?> getReservationFiltered(@PathVariable String userId, @RequestBody FilterDTO filterDTO) {
        try {
            return new ResponseEntity<>(reservationService.getReservationsFiltered(userId, filterDTO), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @GetMapping("reportAll")

    public ResponseEntity<?> getReports(@PathVariable String userId, @RequestBody FilterDTO filterDTO) {
        try {
            return new ResponseEntity<>(reservationService.getReports(userId, filterDTO), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
