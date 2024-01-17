package com.sparklab.TAM.services;

import com.sparklab.TAM.configuration.SmoobuConfiguration;
import com.sparklab.TAM.dto.smoobu.SmoobuGetApartmentsResponse;
import com.sparklab.TAM.dto.apartment.Apartment;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ApartmentService {
    private final ApiCallService apiCallService;
    private final SmoobuConfiguration smoobuConfiguration;
    private static final Logger logger = LogManager.getLogger(ChannelService.class);

    public SmoobuGetApartmentsResponse getAllApartments(String userId) {
        String apiUrl = smoobuConfiguration.getApiURI()+"apartments";
        Long parseUserId =Long.parseLong(userId);
        try {
            return apiCallService.getMethod(apiUrl, SmoobuGetApartmentsResponse.class,parseUserId);

        } catch (ApiCallError e) {

            if (e.getErrorCode() == HttpStatusCode.valueOf(422)) {
                logger.error(e.getMessage(), e);
                throw new ApiCallError(e.getErrorCode(), "Response cannot be parsed as Apartment object");
            }
            logger.error(e.getMessage(), e);
            throw new InternalException();
        }
    }

    public Apartment getApartmentById(String userId,int id) {
        Long parseUserId =Long.parseLong(userId);
        String apiUrl = smoobuConfiguration.getApiURI()+"apartments/" + id;

        try {
            return apiCallService.getMethod(apiUrl, Apartment.class,parseUserId);

        } catch (ApiCallError e) {

            if (e.getErrorCode() == HttpStatusCode.valueOf(422)) {
                logger.error(e.getMessage(), e);
                throw new ApiCallError(e.getErrorCode(), "Response cannot be parsed as Apartment object");
            }
            logger.error(e.getMessage(), e);
            throw new InternalException();
        }
    }
}
