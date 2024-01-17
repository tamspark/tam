package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.OpenAI.Assistant.AssistantCreationResponse;
import com.sparklab.TAM.model.OpenAIAssistant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AssistantCreationResponseToOpenAIAssistant implements Converter<AssistantCreationResponse, OpenAIAssistant> {
    @Override
    public OpenAIAssistant convert(AssistantCreationResponse source) {

        OpenAIAssistant aiAssistant = new OpenAIAssistant();
        aiAssistant.setAssistantId(source.getId());
        aiAssistant.setFileIds(source.getFile_ids());
        aiAssistant.setInstructions(source.getInstructions());
        aiAssistant.setName(source.getName());

        return aiAssistant;
    }
}
