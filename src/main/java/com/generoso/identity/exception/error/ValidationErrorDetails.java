package com.generoso.identity.exception.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrorDetails extends ErrorDetailModel {

    private String field;
    private String fieldMessage;

    @Builder
    public ValidationErrorDetails(String title, int status, String detail, long timestamp, String developerMessage, String field, String fieldMessage) {
        super(title, status, detail, timestamp, developerMessage);
        this.field = field;
        this.fieldMessage = fieldMessage;
    }
}
