package com.generoso.identity.api.controller;

import com.generoso.identity.api.converter.UserV1Converter;
import com.generoso.identity.api.dto.UserV1Dto;
import com.generoso.identity.exception.error.ErrorDetail;
import com.generoso.identity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Tag(name = "UserV1Controller", description = "User management controller")
@RestController
@RequestMapping(path = "v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserV1Controller {

    private final UserService service;
    private final UserV1Converter converter;

    @PostMapping
    @Operation(description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Response body validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "500", description = "Downstream down or unexpected error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            })
    })
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserV1Dto userV1Dto) {
        var user = converter.convertToEntity(userV1Dto);
        service.createUser(user, userV1Dto.password(), userV1Dto.repeatPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
