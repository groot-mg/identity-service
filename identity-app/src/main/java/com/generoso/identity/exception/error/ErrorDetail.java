package com.generoso.identity.exception.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorDetail extends ErrorDetailModel {

    @Builder
    public ErrorDetail(String title, int status, String detail, LocalDateTime localDateTime) {
        super(title, status, detail, localDateTime);
    }
}
