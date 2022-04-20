package com.medium.mcaylak.keycloak.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String userName;
    private String password;

}
