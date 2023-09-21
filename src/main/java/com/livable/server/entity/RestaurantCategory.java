package com.livable.server.entity;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.restaurant.domain.RestaurantErrorCode;

import java.util.Arrays;
import java.util.List;

public enum RestaurantCategory {

    RESTAURANT(List.of("RESTAURANT", "restaurant", "Restaurant")),
    CAFE(List.of("CAFE", "cafe", "Cafe"));

    private final List<String> symbols;

    RestaurantCategory(List<String> symbols) {
        this.symbols = symbols;
    }

    public static RestaurantCategory of(String symbol) {
        return Arrays.stream(values())
                .filter(restaurantCategory -> restaurantCategory.symbols.contains(symbol))
                .findFirst()
                .orElseThrow(() -> new GlobalRuntimeException(RestaurantErrorCode.NOT_FOUND_CATEGORY));
    }
}
