package com.generoso.identity.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailModel {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String detail;
}
