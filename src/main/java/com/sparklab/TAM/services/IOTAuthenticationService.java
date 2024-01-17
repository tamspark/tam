package com.sparklab.TAM.services;

import com.sparklab.TAM.converters.IOTAccountDTOToIOTAccount;
import com.sparklab.TAM.converters.IOTAccountToIOTAccountDTO;
import com.sparklab.TAM.dto.IOTAuth.IOTAccountDTO;
import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.IOTAccount;
import com.sparklab.TAM.repositories.IOTAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IOTAuthenticationService {

    private final IOTAccountRepository iotAccountRepository;
    private final IOTAccountToIOTAccountDTO toIOTAccountDTO;
    private final IOTAccountDTOToIOTAccount toIOTAccount;


    public IOTAccountDTO getByApartment(int apartmentId){

        IOTAccount iotAccount = iotAccountRepository.getByApartmentId(apartmentId).orElseThrow(()->
                new NotFoundException("Apartment with id: " + apartmentId + "does not have an account!"));

        return toIOTAccountDTO.convert(iotAccount);
    }

    public String saveIotAccount(IOTAccountDTO iotAccountDTO){

        IOTAccount iotAccount = iotAccountRepository.save(toIOTAccount.convert(iotAccountDTO));

        return "IOT Account saved successfully!";
    }

}
