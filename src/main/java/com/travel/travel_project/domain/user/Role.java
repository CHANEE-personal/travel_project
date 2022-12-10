package com.travel.travel_project.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_USER("일반유저"),
    ROLE_TRAVEL_USER("여행유저");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
