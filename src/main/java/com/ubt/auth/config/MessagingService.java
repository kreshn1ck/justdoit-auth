package com.ubt.auth.config;

import com.ubt.auth.transport.UserTransport;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessagingService {

    private static final String TOPIC = "USER_CREATED";

    @Autowired
    private final KafkaTemplate<String, UserTransport> kafkaTemplate;

    public void sendUserCreatedMessage(UserTransport user) {
        kafkaTemplate.send(TOPIC, user);
    }
}