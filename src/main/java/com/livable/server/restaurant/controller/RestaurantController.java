package com.livable.server.restaurant.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.ListMenuDTO;
import com.livable.server.restaurant.dto.RestaurantResponse.RestaurantsDto;
import com.livable.server.restaurant.service.RestaurantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurant")
    public ResponseEntity<ApiResponse.Success<Object>> findRestaurantByCategory(
            @RequestParam("type") RestaurantCategory restaurantCategory, @LoginActor Actor actor
            ) {

        JwtTokenProvider.checkVisitorToken(actor);

        Long visitorId = actor.getId();

        List<RestaurantResponse.NearRestaurantDto> result =
                restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(visitorId, restaurantCategory);

        return ApiResponse.success(result, HttpStatus.OK);
    }
  

    @GetMapping("restaurant/{restaurantId}/menus")
    public ResponseEntity<ApiResponse.Success<List<ListMenuDTO>>> sellMenuByRestaurant (
            @PathVariable Long restaurantId,
            @LoginActor Actor actor
            ) {
      
        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        List<ListMenuDTO> result = restaurantService.findMenuList(memberId, restaurantId);

        return ApiResponse.success(result, HttpStatus.OK);
    }
  
    @GetMapping("/restaurants")
    public ResponseEntity<Success<List<RestaurantsDto>>> getRestaurantsByMenu(
            @RequestParam("menuId") Long menuId, @LoginActor Actor actor
            ) {
      
        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();
      
        List<RestaurantsDto> restaurantsDtos = restaurantService.findRestaurantByMenuId(menuId, memberId);

        return ApiResponse.success(restaurantsDtos, HttpStatus.OK);
    }

    @GetMapping("/restaurants/near")
    public ResponseEntity<Success<List<RestaurantsDto>>> getRestaurantsByBuilding(
            @RequestParam("buildingId") Long buildingId, @LoginActor Actor actor
         ) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        List<RestaurantsDto> restaurantsDtos = restaurantService.findRestaurantByBuildingId(buildingId, memberId);

        return ApiResponse.success(restaurantsDtos, HttpStatus.OK);
    }

    @GetMapping("/restaurants/search")
    public ResponseEntity<Success<List<RestaurantResponse.SearchRestaurantsDTO>>> searchRestaurant(
            @RequestParam("query") String keyword,
            @LoginActor Actor actor
    ) {
        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        List<RestaurantResponse.SearchRestaurantsDTO> result = restaurantService.findRestaurantByKeyword(memberId, keyword);

        return ApiResponse.success(result, HttpStatus.OK);
    }

}
