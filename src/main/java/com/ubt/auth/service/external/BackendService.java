package com.ubt.auth.service.external;

import com.netflix.discovery.EurekaClient;
import com.ubt.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BackendService extends ExternalService implements ExternalUrl {

    @Autowired
    public BackendService(EurekaClient discoveryClient, RestTemplate restTemplate) {
        super(discoveryClient, restTemplate);
    }

    @Override
    public String getServiceId() {
        return "BACKEND-SERVICE";
    }

    public void createUser(User user) {
        restTemplate.postForObject(getServiceInstance() + "backend/users/create?id=" + user.getId() +
                        "&email=" + user.getEmail() + "&username=" + user.getUsername(),
                new HttpEntity<>("", null), Void.class);
    }
}