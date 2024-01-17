package com.sparklab.TAM.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photos")
public class Photo extends BaseEntity{

    private String fileName;

    private String fileType;

    @Lob
    @Column( length = 2147483647)
    private byte[] fileBase64;

    private int apartmentId;

}
