package com.generoso.identity.exception.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResourceNotFoundDetails extends ErrorDetailModel {

    @Builder
    public ResourceNotFoundDetails(String title, int status, String detail, long timestamp) {
        super(title, status, detail, timestamp);
    }
}
