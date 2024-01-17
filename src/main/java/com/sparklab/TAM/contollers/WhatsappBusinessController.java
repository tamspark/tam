package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.whatsappApi.responseDTO.WhatsAppMessageResponseDTO;
import com.sparklab.TAM.services.WhatsappBusiness;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("TAM/whatsapp")
public class WhatsappBusinessController {

    private final WhatsappBusiness whatsappBusinessService;


    @PostMapping("/send/{toNumber}/{templateName}")
    public WhatsAppMessageResponseDTO sendMessage(@PathVariable String toNumber,@PathVariable String templateName){
        return whatsappBusinessService.sendMessage(toNumber,templateName);
    }

}
