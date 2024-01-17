package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.IOTAuth.IOTAccountDTO;
import com.sparklab.TAM.services.IOTAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("TAM/iot")
public class IOTAccountController {

    private final IOTAuthenticationService iotAuthenticationService;


    @GetMapping("/{apartmentId}")
    public IOTAccountDTO getByUser(@PathVariable int apartmentId) {
        return iotAuthenticationService.getByApartment(apartmentId);
    }

    @PostMapping
    public ResponseEntity<?> saveIotAccount(@RequestBody IOTAccountDTO iotAccountDTO) {

        return new ResponseEntity<>(iotAuthenticationService.saveIotAccount(iotAccountDTO), HttpStatus.OK);
    }


}
