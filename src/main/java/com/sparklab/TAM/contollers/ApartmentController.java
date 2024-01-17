package com.sparklab.TAM.contollers;

import com.sparklab.TAM.exceptions.ApiCallError;
import com.sparklab.TAM.services.ApartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("TAM/{userId}/apartments")
public class ApartmentController {
    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<?> getAllApartments(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(apartmentService.getAllApartments(userId), HttpStatus.OK);
        } catch (ApiCallError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("{id}")
    public ResponseEntity<?> getApartmentById(@PathVariable String userId,@PathVariable int id){
        try {
            return new ResponseEntity<>(apartmentService.getApartmentById(userId,id),HttpStatus.OK );
        }catch (ApiCallError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

}
