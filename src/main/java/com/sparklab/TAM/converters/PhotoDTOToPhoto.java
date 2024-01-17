package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.PhotoDTO;
import com.sparklab.TAM.model.Photo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class PhotoDTOToPhoto implements Converter<PhotoDTO, Photo> {
    @Override
    public Photo convert(PhotoDTO source) {
        if (source!=null){
            try {
            Photo photo = new Photo();
            if (source.getId() != null){
                photo.setId(source.getId());
            }

            photo.setFileName(photo.getFileName()==null ? StringUtils.cleanPath(source.getFile().getOriginalFilename()) : source.getFileName());
            photo.setFileType(source.getFile().getContentType());
            photo.setFileBase64(source.getFile().getBytes());
            photo.setApartmentId(source.getApartmentId());
            return photo;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
