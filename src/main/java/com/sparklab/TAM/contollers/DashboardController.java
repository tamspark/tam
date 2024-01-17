package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.Dashboard.DashboardDTO;
import com.sparklab.TAM.services.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/TAM/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping("{userId}/{period}")
    public DashboardDTO generateDashboard(@PathVariable String userId, @PathVariable String period){
        return dashboardService.generateDashboard(userId,period);
    }

}
