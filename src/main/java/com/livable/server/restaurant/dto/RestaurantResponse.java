package com.livable.server.restaurant.dto;

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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListMenuDTO {
        private Long menuId;
        private String menuName;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantsDto {

        Long restaurantId;
        String restaurantName;
        Integer tastePercentage;
        String representativeImageUrl;
        String address;
        Integer floor;
        Boolean inBuilding;
        Integer estimatedTime;
        String review;

        public static RestaurantsDto from(RestaurantByMenuProjection restaurantByMenuProjection) {
            return RestaurantsDto.builder()
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
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchRestaurantsDTO {

        private final Long restaurantId;
        private final String restaurantName;
        private final RestaurantCategory restaurantCategory;
        private final Boolean inBuilding;
        private final Integer estimatedTime;
        private final Integer floor;
        private final String thumbnailImageUrl;

        public SearchRestaurantsDTO(
                Long restaurantId,
                String restaurantName,
                RestaurantCategory restaurantCategory,
                Boolean inBuilding,
                String thumbnailImageUrl,
                Integer distance,
                String address
                ) {
                this.restaurantId = restaurantId;
                this.restaurantName = restaurantName;
                this.restaurantCategory = restaurantCategory;
                this.inBuilding = inBuilding;
                this.thumbnailImageUrl = thumbnailImageUrl;
                this.floor = getFloorFromAddress(address);
                this.estimatedTime = calcEstimatedTime(distance);
        }
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

    private static Integer calcEstimatedTime(Integer distance) {
        int averageWalkSpeedPerMin = 80;
        return distance / averageWalkSpeedPerMin;
    }
}
