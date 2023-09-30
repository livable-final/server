package com.livable.server.restaurant.dto;

import com.livable.server.entity.Menu;
import com.livable.server.entity.RestaurantCategory;
import lombok.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantsByMenuDto {

        Long restaurantId;
        String restaurantName;
        Integer tastePercentage;
        String representativeImageUrl;
        String address;
        Integer floor;
        Boolean inBuilding;
        Integer estimatedTime;
        String review;

        public static RestaurantsByMenuDto from(RestaurantByMenuProjection restaurantByMenuProjection) {
            return RestaurantsByMenuDto.builder()
                .restaurantId(restaurantByMenuProjection.getRestaurantId())
                .restaurantName(restaurantByMenuProjection.getRestaurantName())
                .tastePercentage(restaurantByMenuProjection.getTastePercentage())
                .representativeImageUrl(restaurantByMenuProjection.getRestaurantThumbnailUrl())
                .address(restaurantByMenuProjection.getAddress())
                .floor(getFloorFromAddress(restaurantByMenuProjection.getAddress()))
                .inBuilding(restaurantByMenuProjection.getInBuilding())
                .estimatedTime(calcEstimatedTime(restaurantByMenuProjection.getDistance()))
                .review(restaurantByMenuProjection.getReview())
                .build();
        }

        private static Integer calcEstimatedTime(Integer distance) {
            int averageWalkSpeedPerMin = 80;
            return distance / averageWalkSpeedPerMin;
        }

        private static Integer getFloorFromAddress(String address) {

            int floor = 0;

            if (address.contains("층")) {
                String pattern = "\\s(\\d+)층";
                Pattern regexPattern = Pattern.compile(pattern, Pattern.CANON_EQ);
                Matcher matcher = regexPattern.matcher(address);
                if (matcher.find() && matcher.group(1) != null) {

                    floor = Integer.parseInt(matcher.group(1));
                    if (address.contains("지하")) {
                        floor *= -1;
                    }
                }
            }

            return floor;
        }
    }
}
