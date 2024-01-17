package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.IOTAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOTAccountRepository extends JpaRepository<IOTAccount, Long> {

    Optional<IOTAccount> getByUser_Id(Long userId);
    Optional<IOTAccount> getByApartmentId(int apartmentId);
}
