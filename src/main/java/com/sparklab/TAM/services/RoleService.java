package com.sparklab.TAM.services;

import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.Role;
import com.sparklab.TAM.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private static final Logger logger = LogManager.getLogger(RoleService.class);

    public List<Role> findAll() {
        try{
            return roleRepository.findAll();
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No roles found in the database.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalError("We encountered an issue while processing your request. Please try again later. Thank you for your understanding.");
        }
    }
}
