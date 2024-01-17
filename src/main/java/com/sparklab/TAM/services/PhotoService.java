package com.sparklab.TAM.services;

import com.sparklab.TAM.converters.PhotoDTOToPhoto;
import com.sparklab.TAM.converters.PhotoToPhotoDTO;
import com.sparklab.TAM.dto.PhotoDTO;
import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.Photo;
import com.sparklab.TAM.repositories.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoToPhotoDTO toPhotoDTO;
    private final PhotoDTOToPhoto toPhoto;


    public ResponseEntity<?> savePhotos(List<PhotoDTO> photos){
        try {
            List<Photo> photoList = photos.stream().map(toPhoto::convert).toList();
            photoRepository.saveAll(photoList);

            return new ResponseEntity<>("Photo uploaded successfully", HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong, try again!",HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> deleteById(Long id){
        if (photoRepository.existsById(id)){
            photoRepository.deleteById(id);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("The photo you are trying to delete does nto exist!",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Resource> download(Long id){
        Photo photo = photoRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Photo with id: " + id + " not found!"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "photo; filename=\""+ photo.getFileName() + "\"")
                .body(new ByteArrayResource(photo.getFileBase64()));
    }

    public List<PhotoDTO> findByApartmentId(int apartmentId){
        return photoRepository.findAllByApartmentId(apartmentId)
                .stream()
                .map(toPhotoDTO::convert)
                .collect(Collectors.toList());
    }
}
