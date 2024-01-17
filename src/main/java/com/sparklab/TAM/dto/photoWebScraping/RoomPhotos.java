package com.sparklab.TAM.dto.photoWebScraping;

import lombok.Data;

import java.util.List;

@Data
public class RoomPhotos {
    String title;
    String url;

    List<ImageInfoDTO> photos;
}
