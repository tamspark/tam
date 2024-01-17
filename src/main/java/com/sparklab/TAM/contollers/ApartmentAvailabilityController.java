package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.rate.RatesDTO;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sparklab.TAM.services.ApartmentAvailabilityService;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("TAM/{userId}/apartmentAvailability")
@AllArgsConstructor
public class ApartmentAvailabilityController {

    private final ApartmentAvailabilityService apartmentAvailabilityService;

    @PostMapping
    public ResponseEntity<?> saveApartmentAvailability(@RequestBody RatesDTO ratesDTO, @PathVariable String userId){
        try {
            return new ResponseEntity<>(apartmentAvailabilityService.saveApartmentAvailability(ratesDTO, userId), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllRates(@PathVariable String userId, @RequestParam LocalDate start_date,@RequestParam LocalDate end_date,@RequestParam List<Integer> apartments ) {
        try {
            return new ResponseEntity<>(apartmentAvailabilityService.getAllRates(userId,start_date,end_date,apartments), HttpStatus.OK);
        } catch (ApiCallError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping
    public ResponseEntity<?> deletePriceByDates(@RequestParam List<String> dates, @RequestParam Integer apartmentId, @PathVariable String userId){
        RatesDTO ratesToDelete = apartmentAvailabilityService.datesToRatesDTO(dates, apartmentId);
        try {
            return new ResponseEntity<>(apartmentAvailabilityService.saveApartmentAvailability(ratesToDelete, userId), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}


