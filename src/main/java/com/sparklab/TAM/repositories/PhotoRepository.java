package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    List<Photo> findAllByApartmentId(int apartmentId);
}
