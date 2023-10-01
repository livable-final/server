package com.livable.server.restaurant.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.RestaurantsByMenuDto;
import com.livable.server.restaurant.service.RestaurantService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse.Success<List<RestaurantResponse.listMenuDTO>>> sellMenuByRestaurant (
            @PathVariable Long restaurantId,
            @LoginActor Actor actor
            ) {
      
        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        List<RestaurantResponse.listMenuDTO> result = restaurantService.findMenuList(memberId, restaurantId);

        return ApiResponse.success(result, HttpStatus.OK);
    }
  
    @GetMapping("/restaurants/menus/{menuId}")
    public ResponseEntity<Success<List<RestaurantsByMenuDto>>> getRestaurantsByMenu(
            @PathVariable("menuId") Long menuId, @LoginActor Actor actor
            ) {
      
        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();
      
        List<RestaurantsByMenuDto> restaurantsByMenuDtos = restaurantService.findRestaurantByMenuId(menuId, memberId);

        return ApiResponse.success(restaurantsByMenuDtos, HttpStatus.OK);
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
