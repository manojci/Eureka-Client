package com.bfs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class EurekaClientApplication {

    public static final String CITY_SEARCH = "CITY-SEARCH";
    
    public static final String TRAVEL_DETAILS = "TRAVEL-DETAILS";
    
    public static final String USER_REGISTER_SERVICE = "USER-REGISER-SERVICE";

    @Autowired
    private DiscoveryClient discoveryClient;

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @RequestMapping(value = "/v1/cities", method = RequestMethod.GET)
    public List<Object> getCities() {
        List<Object> result = new ArrayList<Object>();
        List<ServiceInstance> serviceInstances= this.discoveryClient.getInstances(CITY_SEARCH);
        String restURI = null;
        for (ServiceInstance ServiceInstance : serviceInstances) {
            if (ServiceInstance.getServiceId().equals(CITY_SEARCH)) {
                restURI = ServiceInstance.getUri().toString() + "/" + "v1/trip/city-search";
            }
        }
        result.add(restURI);
        return result;
    }

    @RequestMapping(value = "/v1/travel-details", method = RequestMethod.GET)
    public List<Object> getTravelDetails() {
        List<Object> result = new ArrayList<Object>();
        List<ServiceInstance> serviceInstances= this.discoveryClient.getInstances(TRAVEL_DETAILS);
        String restURI = null;
        for (ServiceInstance ServiceInstance : serviceInstances) {
            if (ServiceInstance.getServiceId().equals(TRAVEL_DETAILS)) {
                restURI = ServiceInstance.getUri().toString() + "/" + "v1/trip/travel-details";
            }
        }
        result.add(restURI);
        return result;
    }

    @RequestMapping(value = "/v1/save", method = RequestMethod.GET)
    public List<Object> registerUser() {
        List<Object> result = new ArrayList<Object>();
        List<ServiceInstance> serviceInstances= this.discoveryClient.getInstances(USER_REGISTER_SERVICE);
        String restURI = null;
        for (ServiceInstance ServiceInstance : serviceInstances) {
            if (ServiceInstance.getServiceId().equals(USER_REGISTER_SERVICE)) {
                restURI = ServiceInstance.getUri().toString() + "/" + "v1/user/save";
            }
        }
        result.add(restURI);
        return result;
    }
}