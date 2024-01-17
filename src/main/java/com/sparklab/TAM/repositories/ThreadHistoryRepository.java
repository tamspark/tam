package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.ThreadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreadHistoryRepository extends JpaRepository<ThreadHistory,Long > {
    ThreadHistory findThreadHistoryByThreadId(String threadId);
}
