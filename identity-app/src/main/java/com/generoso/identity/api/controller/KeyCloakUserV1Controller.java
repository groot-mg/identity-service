package com.generoso.identity.api.controller;

import com.generoso.identity.api.converter.UserV1Converter;
import com.generoso.identity.api.dto.UserV1Dto;
import com.generoso.identity.service.KeyCloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "v1/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyCloakUserV1Controller {

    private final KeyCloakService service;
    private final UserV1Converter converter;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserV1Dto userV1Dto) {
        var user = converter.convertToEntity(userV1Dto);
        service.createUser(user, userV1Dto.password());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{username}")
    public ResponseEntity<UserV1Dto> getUser(@PathVariable("username") String username) {
        var user = service.getUser(username);
        return ResponseEntity.ok(converter.convertToDto(user));
    }

    // review - changing password? changing email? - should not change password here
    @PutMapping(path = "/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @RequestBody UserV1Dto userV1Dto) {
        var user = converter.convertToEntity(userV1Dto);
        service.updateUser(userId, user);
        return "User Details Updated Successfully.";
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/verification-link/{userId}")
    public ResponseEntity<Void> sendVerificationLink(@PathVariable("userId") String userId) {
        service.sendVerificationLink(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/reset-password/{userId}")
    public ResponseEntity<Void> sendResetPassword(@PathVariable("userId") String userId) {
        service.sendResetPassword(userId);
        return ResponseEntity.ok().build();
    }
}
