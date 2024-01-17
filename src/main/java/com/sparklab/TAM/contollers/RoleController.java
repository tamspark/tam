package com.sparklab.TAM.contollers;

import com.sparklab.TAM.model.Role;
import com.sparklab.TAM.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("TAM/role")
@AllArgsConstructor
public class RoleController {


    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> findAllRoles() {
        try {
            return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(roleService.findAll(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
