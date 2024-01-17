package com.sparklab.TAM.services;

import com.sparklab.TAM.converters.PlatformToPlatformDTO;
import com.sparklab.TAM.dto.smoobu.ChannelDTO;
import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.Channel;
import com.sparklab.TAM.repositories.PlatformRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChannelService {


    private final PlatformRepository platformRepository;

    private final PlatformToPlatformDTO toPlatformDTO;
    private static final Logger logger = LogManager.getLogger(ChannelService.class);


    public List<ChannelDTO> findAllPlatforms() {

        try {
            List<Channel> platforms = platformRepository.findAll();
            if (platforms.size() != 0) {
                return platforms.stream().map(platform -> toPlatformDTO.convert(platform)).collect(Collectors.toList());
            }
            throw new NotFoundException("There is no platform available.");
        } catch (DataAccessException e) {
            logger.error(e.getMessage(), e);
            throw new InternalError("We encountered an error while retrieving data from the database.");
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalError("We encountered an issue while processing your request.");
        }
    }
}
