package com.generoso.identity.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthV1Controller {

    @PostMapping
    public ResponseEntity<Void> login() {
        return null;
    }
}
