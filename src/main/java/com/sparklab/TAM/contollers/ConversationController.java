package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.smoobu.ConversationDTO;
import com.sparklab.TAM.services.ConversationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("TAM/conversation/{userId}")
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    public List<ConversationDTO> findAll(@PathVariable String userId){
        return conversationService.getAllConversations(userId);
    }

}
