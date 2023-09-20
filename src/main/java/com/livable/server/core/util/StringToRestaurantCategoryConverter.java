package com.livable.server.core.util;

import com.livable.server.entity.RestaurantCategory;
import org.springframework.core.convert.converter.Converter;

public class StringToRestaurantCategoryConverter implements Converter<String, RestaurantCategory> {

    @Override
    public RestaurantCategory convert(String event) {
        return RestaurantCategory.of(event);
    }
}
