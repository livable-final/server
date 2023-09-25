package com.livable.server.restaurant.controller;

import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.mock.MockNearRestaurantDto;
import com.livable.server.restaurant.service.RestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}