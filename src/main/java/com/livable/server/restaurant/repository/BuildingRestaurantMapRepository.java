package com.livable.server.restaurant.repository;

import com.livable.server.entity.BuildingRestaurantMap;
import com.livable.server.entity.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRestaurantMapRepository extends JpaRepository<BuildingRestaurantMap, Long> {

    Integer countBuildingRestaurantMapByBuildingIdAndRestaurant_RestaurantCategory(
            Long buildingId,
            RestaurantCategory restaurantCategory
    );
}
