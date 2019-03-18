package com.bfs.service;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableCircuitBreaker
public class EurekaClientApplication {

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApplication.class, args);
	}

	@HystrixCommand(fallbackMethod = "fallbackMethod")
	@RequestMapping(path = "/client", method = RequestMethod.GET)
	public String getClient() throws MalformedURLException {
		ServiceInstance si = loadBalancerClient.choose("bookservice");
		String result = getRestTemplate()
				.exchange(si.getUri().toURL().toString() + "/" + "books", HttpMethod.GET, null, String.class).getBody();
		return result;
	}

	public String fallbackMethod() {
		return "Service is Down";
	}
}