package com.ubt.auth.transport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTransport {

    private Long id;
    private String username;
    private String email;
    private String confirmToken;
}