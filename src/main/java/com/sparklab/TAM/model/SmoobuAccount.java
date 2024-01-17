package com.sparklab.TAM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "smoobu_account")
public class SmoobuAccount extends BaseEntity {

    private String clientId;
    private String clientAPIKey;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id",nullable = false)
    private User user;
}
