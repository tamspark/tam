package com.sparklab.TAM.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SmoobuConfiguration {

    @Value("${smoobu.api.url}")
    private String apiURI;
}
