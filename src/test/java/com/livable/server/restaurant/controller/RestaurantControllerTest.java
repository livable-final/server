package com.livable.server.restaurant.controller;

import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.service.RestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(RestaurantController.class)
@AutoConfigureRestDocs
class RestaurantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider tokenProvider;

    @MockBean
    RestaurantService restaurantService;

    @DisplayName("[GET][/api/restaurant?type={category}] - 빌딩 내, 근처 식당 정상 응답")
    @CsvSource(value = {"RESTAURANT", "Restaurant", "restaurant"})
    @ParameterizedTest(name = "[{index}] category={0}")
    void findRestaurantByCategorySuccessTest_RESTAURANT(String category) throws Exception {
        // given
        String token = tokenProvider.createActorToken(ActorType.VISITOR, 1L, new Date(new Date().getTime() + 10000000));

        List<RestaurantResponse.NearRestaurantDto> result =
                IntStream.range(1, 10)
                        .mapToObj(idx -> new RestaurantResponse.NearRestaurantDto(
                                RestaurantCategory.of(category),
                                category + idx,
                                "restaurantImageUrl" + idx,
                                idx % 2 == 0,
                                idx,
                                idx,
                                "pageUrl" + idx
                        ))
                        .collect(Collectors.toList());

        given(restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(
                1L, RestaurantCategory.of(category)
        )).willReturn(result);


        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/restaurant")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("type", category)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].restaurantCategory").isString())
                .andExpect(jsonPath("$.data[0].restaurantName").isString())
                .andExpect(jsonPath("$.data[0].restaurantImageUrl").isString())
                .andExpect(jsonPath("$.data[0].inBuilding").isBoolean())
                .andExpect(jsonPath("$.data[0].takenTime").isNumber())
                .andExpect(jsonPath("$.data[0].floor").isNumber())
                .andExpect(jsonPath("$.data[0].url").isString())
                .andDo(document(
                        "{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(parameterWithName("type").description("가게 종류")),
                        responseFields(
                                fieldWithPath("data[].restaurantCategory").type(JsonFieldType.STRING).description("가게 종류"),
                                fieldWithPath("data[].restaurantName").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("data[].restaurantImageUrl").type(JsonFieldType.STRING).description("가게 썸네일 이미지"),
                                fieldWithPath("data[].inBuilding").type(JsonFieldType.BOOLEAN).description("빌딩 내 존재 여부"),
                                fieldWithPath("data[].takenTime").type(JsonFieldType.NUMBER).description("걸리는 시간"),
                                fieldWithPath("data[].floor").type(JsonFieldType.NUMBER).description("층에 대한 정보"),
                                fieldWithPath("data[].url").type(JsonFieldType.STRING).description("페이지 URL")
                        )
                ));
    }

    @DisplayName("[GET][/api/restaurant?type={category}] - 빌딩 내, 근처 식당 정상 응답")
    @CsvSource(value = {"CAFE", "Cafe", "cafe"})
    @ParameterizedTest(name = "[{index}] category={0}")
    void findRestaurantByCategorySuccessTest_CAFE(String category) throws Exception {
        // given
        String token = tokenProvider.createActorToken(ActorType.VISITOR, 1L, new Date(new Date().getTime() + 10000000));

        List<RestaurantResponse.NearRestaurantDto> result =
                IntStream.range(1, 10)
                        .mapToObj(idx -> new RestaurantResponse.NearRestaurantDto(
                                RestaurantCategory.of(category),
                                category + idx,
                                "restaurantImageUrl" + idx,
                                idx % 2 == 0,
                                idx,
                                idx,
                                "pageUrl" + idx
                        ))
                        .collect(Collectors.toList());

        given(restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(
                1L, RestaurantCategory.of(category)
        )).willReturn(result);


        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/restaurant")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("type", category)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].restaurantCategory").isString())
                .andExpect(jsonPath("$.data[0].restaurantName").isString())
                .andExpect(jsonPath("$.data[0].restaurantImageUrl").isString())
                .andExpect(jsonPath("$.data[0].inBuilding").isBoolean())
                .andExpect(jsonPath("$.data[0].takenTime").isNumber())
                .andExpect(jsonPath("$.data[0].floor").isNumber())
                .andExpect(jsonPath("$.data[0].url").isString())
                .andDo(document(
                        "{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(parameterWithName("type").description("가게 종류")),
                        responseFields(
                                fieldWithPath("data[].restaurantCategory").type(JsonFieldType.STRING).description("가게 종류"),
                                fieldWithPath("data[].restaurantName").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("data[].restaurantImageUrl").type(JsonFieldType.STRING).description("가게 썸네일 이미지"),
                                fieldWithPath("data[].inBuilding").type(JsonFieldType.BOOLEAN).description("빌딩 내 존재 여부"),
                                fieldWithPath("data[].takenTime").type(JsonFieldType.NUMBER).description("걸리는 시간"),
                                fieldWithPath("data[].floor").type(JsonFieldType.NUMBER).description("층에 대한 정보"),
                                fieldWithPath("data[].url").type(JsonFieldType.STRING).description("페이지 URL")
                        )
                ));
    }
}