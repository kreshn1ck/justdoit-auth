package com.ubt.auth.service.external;

import com.netflix.discovery.EurekaClient;
import org.springframework.web.client.RestTemplate;

public abstract class ExternalService implements ExternalUrl {

    protected EurekaClient discoveryClient;
    protected RestTemplate restTemplate;

    public ExternalService(EurekaClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public String getServiceInstance() {
        return discoveryClient.getNextServerFromEureka(getServiceId(), false).getHomePageUrl();
    }
}