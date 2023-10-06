package com.livable.server.restaurant.repository;

import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.ListMenuDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantCustomRepository {

    List<RestaurantResponse.NearRestaurantDto> findRestaurantByBuildingIdAndRestaurantCategory(
            Long buildingId,
            RestaurantCategory category,
            Pageable pageable
    );

    List<ListMenuDTO> findMenuList(Long restaurantId);

    List<RestaurantResponse.SearchRestaurantsDTO> findRestaurantByKeyword(Long buildingId, String keyword);
}
