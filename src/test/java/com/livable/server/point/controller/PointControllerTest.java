package com.livable.server.point.controller;

import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Date;

@Import(TestConfig.class)
@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockBean
    private PointService pointService;

    @Nested
    @DisplayName("나의 리뷰 카운트 컨트롤러 단위 테스트")
    class MyRestaurantReview {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/points/logs/members";
            String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

            PointResponse.ReviewCountDTO mockResponse
                    = new PointResponse.ReviewCountDTO(5L);

            Mockito.when(pointService.getMyReviewCount(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(mockResponse);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .header("Authorization", "Bearer " + token)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
        }
    }

    @Nested
    @DisplayName("목표 달성 포인트 지급 컨트롤러 단위 테스트")
    class GetAchievementPoint {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/points/logs/members";
            String token = tokenProvider.createActorToken(ActorType.MEMBER, 1L, new Date(new Date().getTime() + 10000000));

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.post(uri)
                            .header("Authorization", "Bearer " + token)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }
}