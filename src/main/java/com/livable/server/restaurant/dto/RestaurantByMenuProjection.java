package com.livable.server.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantByMenuProjection {

	Long restaurantId;
	String restaurantName;
	String restaurantThumbnailUrl;
	String address;
	Boolean inBuilding;
	Integer distance;
	String review;
	Integer tastePercentage;

}
