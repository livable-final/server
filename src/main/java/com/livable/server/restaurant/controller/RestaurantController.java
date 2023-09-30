package com.livable.server.restaurant.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.service.RestaurantService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<ApiResponse.Success<Object>> findRestaurantByCategory(
            @RequestParam("type") RestaurantCategory restaurantCategory, @LoginActor Actor actor
            ) {

        JwtTokenProvider.checkVisitorToken(actor);

        Long visitorId = actor.getId();

        List<RestaurantResponse.NearRestaurantDto> result =
                restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(visitorId, restaurantCategory);

        return ApiResponse.success(result, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<ApiResponse.Success<List>> sellMenuByRestaurant (
            @PathVariable Long restaurantId,
            @LoginActor Actor actor
            ) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        List<RestaurantResponse.listMenuDTO> result = restaurantService.findMenuList(memberId, restaurantId);

        return ApiResponse.success(result, HttpStatus.OK);
    }
}
