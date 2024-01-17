package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Channel, Long> {



}
