package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.MessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageHistoryRepository extends JpaRepository<MessageHistory,Long> {


}
