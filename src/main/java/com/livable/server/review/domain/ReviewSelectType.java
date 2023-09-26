package com.livable.server.review.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReviewSelectType {
    LUNCH_BOX("도시락"),
    CAFETERIA("구내식당");

    private final String message;

    public String getMessage() {
        return message;
    }
}
