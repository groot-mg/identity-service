package com.generoso.identity.api.controller;

import com.generoso.identity.api.dto.LoginResponseV1Dto;
import com.generoso.identity.api.dto.LoginV1Dto;
import com.generoso.identity.exception.error.ErrorDetail;
import com.generoso.identity.exception.error.ValidationErrorDetails;
import com.generoso.identity.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "AuthV1Controller", description = "Authentication controller")
@RestController
@RequestMapping(path = "v1/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthV1Controller {

    private final AuthService authService;

    @PostMapping
    @Operation(description = "Login in the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Response body validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))
            }),
            @ApiResponse(responseCode = "500", description = "Downstream down or unexpected error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            })
    })
    public ResponseEntity<LoginResponseV1Dto> login(@Valid @RequestBody LoginV1Dto loginV1Dto) {
        var response = authService.login(loginV1Dto.username(), loginV1Dto.password());
        return ResponseEntity.ok(new LoginResponseV1Dto(response.getTokenType(), response.getToken()));
    }
}