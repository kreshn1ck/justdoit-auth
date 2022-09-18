package com.ubt.auth.service.external;

import com.netflix.discovery.EurekaClient;
import com.ubt.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService extends ExternalService implements ExternalUrl {

    @Autowired
    public EmailService(EurekaClient discoveryClient, RestTemplate restTemplate) {
        super(discoveryClient, restTemplate);
    }

    @Override
    public String getServiceId() {
        return "EMAILS-SERVICE";
    }

    public void sendUserConfirmationEmail(User user, String userToken) {
        restTemplate.postForObject(getServiceId() + "/emails/confirm?email=" + user.getEmail() + "&confirmToken=" +
                userToken + "&username=" + user.getUsername(), new HttpEntity<>("", null), Void.class);
        System.out.println("asd");
    }

    public void sendForgotPasswordEmail(User user, String resetToken) {
        restTemplate.postForObject("http://localhost:9191/emails/forgot-password?email=" + user.getEmail() + "&resetToken=" +
                resetToken + "&username=" + user.getUsername(), new HttpEntity<>("", null), Void.class);
        System.out.println("asd");
    }
}