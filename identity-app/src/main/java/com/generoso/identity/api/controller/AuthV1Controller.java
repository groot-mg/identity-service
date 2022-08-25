package com.generoso.identity.api.controller;

import com.generoso.identity.api.dto.LoginResponseV1Dto;
import com.generoso.identity.api.dto.LoginV1Dto;
import com.generoso.identity.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthV1Controller {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<LoginResponseV1Dto> login(@Valid @RequestBody LoginV1Dto loginV1Dto) {
        var response = authService.login(loginV1Dto.username(), loginV1Dto.password());
        return ResponseEntity.ok(new LoginResponseV1Dto(response.getTokenType(), response.getToken()));
    }
}