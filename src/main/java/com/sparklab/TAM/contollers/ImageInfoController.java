package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.photoWebScraping.ImageInfoDTO;
import com.sparklab.TAM.dto.photoWebScraping.RoomPhotos;
import com.sparklab.TAM.services.ImageInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("TAM/image-info")
public class ImageInfoController {

    private final ImageInfoService imageInfoService;

    public ImageInfoController(ImageInfoService imageInfoService) {
        this.imageInfoService = imageInfoService;
    }

    @GetMapping()
    public ResponseEntity<List<RoomPhotos>> getImageInfo(@RequestParam String url) {
        try {
            List<RoomPhotos> roomPhotos = imageInfoService.extractImageInfo(url);
            return ResponseEntity.ok(roomPhotos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
