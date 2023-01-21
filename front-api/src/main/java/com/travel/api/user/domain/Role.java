package com.travel.api.user.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_TRAVEL_USER("여행 유저");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
