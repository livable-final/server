package com.livable.server.restaurant.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.domain.RestaurantErrorCode;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.RestaurantsDto;
import com.livable.server.restaurant.mock.MockNearRestaurantDto;
import com.livable.server.restaurant.mock.MockRestaurantDto;
import com.livable.server.restaurant.service.RestaurantService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Import(TestConfig.class)
@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider tokenProvider;

    @MockBean
    RestaurantService restaurantService;

    @DisplayName("[GET][/api/restaurant?type={category}] - 빌딩 내, 근처 식당 정상 응답")
    @CsvSource(value = {"CAFE", "RESTAURANT", "restaurant", "Restaurant", "Cafe", "cafe"})
    @ParameterizedTest(name = "[{index}] category={0}")
    void findRestaurantByCategorySuccessTest(String category) throws Exception {
        // given
        String token = tokenProvider.createActorToken(ActorType.VISITOR, 1L, new Date(new Date().getTime() + 10000000));

        List<RestaurantResponse.NearRestaurantDto> result =
                IntStream.range(1, 10)
                        .mapToObj(idx -> new MockNearRestaurantDto())
                        .collect(Collectors.toList());

        given(restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(
                1L, RestaurantCategory.of(category)
        )).willReturn(result);


        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/restaurant")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("type", "restaurant")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("SUCCESS - 메뉴별 식당 목록 응답 컨트롤러 테스트")
    @Test
    void findRestaurantByMenuSuccess() throws Exception {

        // given
        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

        List<RestaurantsDto> result =
            IntStream.range(1, 10)
                .mapToObj(idx -> new MockRestaurantDto())
                .collect(Collectors.toList());

        given(restaurantService.findRestaurantByMenuId(anyLong(), anyLong()))
            .willReturn(result);

        // when & then
        mockMvc.perform(get("/api/restaurants?menuId=1")
            .header("Authorization", "Bearer " + token)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());

    }

    @DisplayName("FAILED - 메뉴별 식당 목록 응답 컨트롤러 테스트")
    @Test
    void findRestaurantByMenuFailed() throws Exception {

        //given
        String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L,
            new Date(new Date().getTime() + 10000000));

        given(restaurantService.findRestaurantByMenuId(anyLong(), anyLong()))
            .willThrow(
                new GlobalRuntimeException(RestaurantErrorCode.NOT_FOUND_RESTAURANT_BY_MENU));

        // when & then
        mockMvc.perform(get("/api/restaurants?menuId=1")
                .header("Authorization", "Bearer " + token)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(
                RestaurantErrorCode.NOT_FOUND_RESTAURANT_BY_MENU.getMessage()));
    }
}