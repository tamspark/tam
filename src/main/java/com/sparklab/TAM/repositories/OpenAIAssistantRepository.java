package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.OpenAIAssistant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenAIAssistantRepository extends JpaRepository<OpenAIAssistant,Long> {
}
