package com.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
@Configuration
public class ConfigProperties {

    @Autowired
    public ConfigProperties(@Value("${app.env}") String environmentName, @Value("${server.port}") String port){

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(Calendar.getInstance().getTime());
        System.out.println();
        System.out.println(timeStamp + "  :   ENVIRONMENT NAME----> " + environmentName + " (Port : "+port+")");
        System.out.println();
    }
}
