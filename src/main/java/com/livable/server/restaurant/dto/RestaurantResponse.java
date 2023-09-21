package com.livable.server.restaurant.dto;

import com.livable.server.entity.RestaurantCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestaurantResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NearRestaurantDto {

        private RestaurantCategory restaurantCategory;
        private String restaurantName;
        private String restaurantImageUrl;

        private Boolean inBuilding;

        private Integer takenTime;
        private Integer floor;

        private String url;
    }
}
