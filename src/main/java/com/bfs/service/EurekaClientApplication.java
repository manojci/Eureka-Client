package com.bfs.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class EurekaClientApplication {

    public static final String APP_NAME = "CITYSEARCH";

    @Autowired
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @RequestMapping(value = "/getCities", method = RequestMethod.GET)
    public Map<String,String> getCities() {
        Map<String, String> result = null;
        List<ServiceInstance> serviceInstances= this.discoveryClient.getInstances(APP_NAME);
        String restURI = null;
        for (ServiceInstance ServiceInstance : serviceInstances) {
            if (ServiceInstance.getServiceId().equals(APP_NAME)) {
                restURI = ServiceInstance.getUri().toString() + "/" + "citySearch";
            }
        }
        if (StringUtils.isNotEmpty(restURI)) {
            result = restTemplate.getForObject(restURI, Map.class);
        }
        return result;
    }
}