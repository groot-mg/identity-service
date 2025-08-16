package com.generoso.identity.exception.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorDetail extends ErrorDetailModel {

    @Builder
    public ErrorDetail(String error, int status, String detail) {
        super(LocalDateTime.now(), status, error, detail);
    }
}
