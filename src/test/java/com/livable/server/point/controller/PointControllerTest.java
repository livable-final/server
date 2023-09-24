package com.livable.server.point.controller;

import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.service.PointService;
import com.livable.server.review.dto.MyReviewResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

            PointResponse.ReviewCountDTO mockResponse
                    = new PointResponse.ReviewCountDTO(5L);

            Mockito.when(pointService.getMyReviewCount(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(mockResponse);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
        }
    }
}