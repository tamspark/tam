package com.sparklab.TAM.repositories;


import com.sparklab.TAM.model.SmoobuAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SmoobuAccountRepository extends JpaRepository<SmoobuAccount, Long> {

    boolean existsByClientId(String clientId);
    Optional<SmoobuAccount> findByUser_Id(Long id);
    boolean existsByUser_Id(Long id);
}
