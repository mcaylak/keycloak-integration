package com.medium.mcaylak.keycloak.service;

import com.medium.mcaylak.keycloak.config.KeycloakConfig;
import com.medium.mcaylak.keycloak.request.CreateUserRequest;
import com.medium.mcaylak.keycloak.request.LoginRequest;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class KeyCloakService {

    @Value("${key-cloak-server-url}")
    private String loginUrl;
    @Value("${key-cloak-secret-key}")
    private String clientSecret;
    @Value("${app.keycloak.grant-type}")
    private String grantType ;
    @Value("${key-cloak-client-id}")
    private String clientId;
    @Value("${key-cloak-server-url-register}")
    private String serverUrl;
    @Value("${key-cloak-realm}")
    private String realm;
    @Value("${key-cloak-username}")
    private String userName;
    @Value("${key-cloak-password}")
    private String password;


    public ResponseEntity<AccessTokenResponse> login (LoginRequest request)  {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", request.getUserName());
        map.add("password", request.getPassword());
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<AccessTokenResponse> loginResponse = new RestTemplate().postForEntity(loginUrl, httpEntity,AccessTokenResponse.class);

        return ResponseEntity.status(200).body(loginResponse.getBody());

    }

    public void register(CreateUserRequest createUserRequest){
        CredentialRepresentation credential = createPasswordCredentials(createUserRequest.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(createUserRequest.getUserName());
//        user.setFirstName(createUserRequest.getFirstname());
//        user.setLastName(createUserRequest.getLastName());
//        user.setEmail(createUserRequest.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = new KeycloakConfig().getInstance(serverUrl,realm,userName,password,clientId,clientSecret).realm("medium-test").users();
        instance.create(user);
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
