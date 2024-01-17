package com.sparklab.TAM.contollers;


import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.services.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("TAM/platform")
@AllArgsConstructor
public class PlatformController {

    private final ChannelService platformService;


    @GetMapping
    public ResponseEntity<?> findAllPlatforms() {

        try {
            return new ResponseEntity<>(platformService.findAllPlatforms(), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
