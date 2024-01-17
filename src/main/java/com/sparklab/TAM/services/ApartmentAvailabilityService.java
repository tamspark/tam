package com.sparklab.TAM.services;

import com.sparklab.TAM.configuration.SmoobuConfiguration;
import com.sparklab.TAM.dto.GetRatesDTO;
import com.sparklab.TAM.dto.RatesRequestDTO;
import com.sparklab.TAM.dto.rate.RatesDTO;
import com.sparklab.TAM.dto.rate.RatesResponseDTO;
import com.sparklab.TAM.dto.reservation.Operation;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApartmentAvailabilityService {

    private static final Logger logger = LogManager.getLogger(ApartmentAvailabilityService.class);
    private final SmoobuConfiguration smoobuConfiguration;
    private final ApiCallService apiCallService;

    public String saveApartmentAvailability(RatesDTO ratesDTO, String userId) {

        try {
            long parseUserId = Long.parseLong(userId);
            String apiUrl = smoobuConfiguration.getApiURI() + "rates";
           if(apiCallService.postMethod(apiUrl, RatesResponseDTO.class, parseUserId, ratesDTO).isSuccess())
               return "The apartment's rate price for the specified dates is saved successfully";
            throw new InternalException("An error occurred while saving apartment availability.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("An error occurred while saving apartment availability.");
        }

    }


    public GetRatesDTO getAllRates(String userId, LocalDate start_date,  LocalDate end_date,  List<Integer> apartments ) throws ApiCallError, InternalException {

        try {
            Long parseUserId = Long.parseLong(userId);

            String apartmentIds = "apartments[]=" + apartments.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("&apartments[]="));
            String apiUrl = smoobuConfiguration.getApiURI() + "rates?" + apartmentIds
                    + "&start_date=" + start_date + "&end_date="
                    + end_date;
            GetRatesDTO originalRatesDTO = apiCallService.getMethod(apiUrl, GetRatesDTO.class, parseUserId);
            originalRatesDTO.filterDateByPrice(originalRatesDTO.getData());

            return originalRatesDTO;
        } catch (ApiCallError e) {
            if (e.getErrorCode() == HttpStatusCode.valueOf(422)) {
                logger.error(e.getMessage(), e);
                throw new ApiCallError(e.getErrorCode(), "Response cannot be parsed as Rates object");
            }
            logger.error(e.getMessage(), e);
            throw new InternalException();
        }
    }
    public RatesDTO datesToRatesDTO(List<String> dates, Integer apartmentId) {

        RatesDTO ratesToDelete = new RatesDTO();
        List<Integer> apartments = new ArrayList<>();
        apartments.add(apartmentId);
        ratesToDelete.setApartments(apartments);
        List<Operation> operationList = new ArrayList<>();
        Operation operation = new Operation();
        operation.setDates(dates);
        operation.setDaily_price(0);

        operationList.add(operation);

        ratesToDelete.setOperations(operationList);

        return ratesToDelete;
    }
}
