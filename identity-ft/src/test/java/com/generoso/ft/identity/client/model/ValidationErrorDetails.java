package com.generoso.ft.identity.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ValidationErrorDetails extends ErrorDetailModel {

    private String field;
    private String fieldMessage;

    @Builder
    public ValidationErrorDetails(String error, int status, String detail, String field, String fieldMessage) {
        super(LocalDateTime.now(), status, error, detail);
        this.field = field;
        this.fieldMessage = fieldMessage;
    }
}
