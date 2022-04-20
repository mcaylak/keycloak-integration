package com.medium.mcaylak.keycloak.controller;

import com.medium.mcaylak.keycloak.request.CreateUserRequest;
import com.medium.mcaylak.keycloak.request.LoginRequest;
import com.medium.mcaylak.keycloak.service.KeyCloakService;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/key-cloak")
public class KeyCloakController {

    private final KeyCloakService keyCloakService;
    Logger logger = LoggerFactory.getLogger(KeyCloakController.class);


    public KeyCloakController(KeyCloakService keyCloakService) {
        this.keyCloakService = keyCloakService;
    }

    @PostMapping("/register")
    public String register(@RequestBody CreateUserRequest createUserRequest){
        try{
            keyCloakService.register(createUserRequest);
        }catch (Exception e){
            logger.error("Registration failed! {}",e.getMessage());
        }

        return "Registration completed!";
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginRequest loginRequest){
        return keyCloakService.login(loginRequest);
    }

}
