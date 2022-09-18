package com.ubt.auth.mapper;

import com.ubt.auth.model.User;
import com.ubt.auth.transport.UserTransport;

import java.util.List;
import java.util.stream.Collectors;

public final class UserTransportMapper {

    private UserTransportMapper() {
    }

    public static UserTransport toTransport(User user) {
        if (user == null) {
            return null;
        }
        UserTransport userTransport = new UserTransport();
        userTransport.setId(user.getId());
        userTransport.setEmail(user.getEmail());
        userTransport.setUsername(user.getUsername());
        userTransport.setConfirmToken(user.getConfirmToken());
        return userTransport;
    }

    public static List<UserTransport> toTransportList(List<User> users) {
        return users.stream().map(UserTransportMapper::toTransport).collect(Collectors.toList());
    }
}
