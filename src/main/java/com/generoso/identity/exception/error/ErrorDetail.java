package com.generoso.identity.exception.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorDetail extends ErrorDetailModel {

    @Builder
    public ErrorDetail(String title, int status, String detail, long timestamp) {
        super(title, status, detail, timestamp);
    }
}
