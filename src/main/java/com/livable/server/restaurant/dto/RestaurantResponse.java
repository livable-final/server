package com.livable.server.restaurant.dto;

import com.livable.server.entity.Menu;
import com.livable.server.entity.RestaurantCategory;
import lombok.*;

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

    @Getter
    @AllArgsConstructor
    public static class listMenuDTO {
        private Long menuId;
        private String menuName;
    }
}
