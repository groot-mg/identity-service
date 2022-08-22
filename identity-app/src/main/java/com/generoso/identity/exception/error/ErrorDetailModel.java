package com.generoso.identity.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorDetailModel {

    private final String title;
    private final int status;
    private final String detail;
    private final LocalDateTime localDateTime;
}
