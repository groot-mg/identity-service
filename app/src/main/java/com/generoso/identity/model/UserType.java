package com.generoso.identity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    CLIENT("Clients"),
    SALES("Sales");

    private final String groupName;
}
