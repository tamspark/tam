package com.sparklab.TAM.contollers;

import com.sparklab.TAM.services.CheckInService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/TAM/checkin")
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("{userId}/{apartmentId}")
    public ResponseEntity<?> doCheckIn(@PathVariable String userId, @PathVariable int apartmentId) {
        return checkInService.doCheckIn(userId, apartmentId);
    }

}
