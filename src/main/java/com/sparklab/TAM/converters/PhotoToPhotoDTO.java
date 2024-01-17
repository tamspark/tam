package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.PhotoDTO;
import com.sparklab.TAM.model.Photo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class PhotoToPhotoDTO implements Converter<Photo, PhotoDTO> {
    @Override
    public PhotoDTO convert(Photo source) {
        if (source!=null){
            PhotoDTO photoDTO = new PhotoDTO();

            photoDTO.setFileName(source.getFileName());
            photoDTO.setFileType(source.getFileType());
            photoDTO.setFileByteArray(source.getFileBase64());
            photoDTO.setDownloadURL(generateDownloadUrl(source.getId()));
            photoDTO.setApartmentId(source.getApartmentId());

            return photoDTO;
        }
        return null;
    }

    private String generateDownloadUrl(Long photoId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("TAM/photo/download/")
                .path(String.valueOf(photoId))
                .toUriString();
    }
}
