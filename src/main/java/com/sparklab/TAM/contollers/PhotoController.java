package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.PhotoDTO;
import com.sparklab.TAM.services.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/TAM/photo")
public class PhotoController {
    private final PhotoService photoService;


    @PostMapping
    public ResponseEntity<?> savePhotos(@ModelAttribute List<PhotoDTO> photos){
        return photoService.savePhotos(photos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        return photoService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id){
        return photoService.download(id);
    }

    @GetMapping("/apartment/{apartmentId}")
    public List<PhotoDTO> findByApartmentId(@PathVariable int apartmentId){
        return photoService.findByApartmentId(apartmentId);
    }

}
