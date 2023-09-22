package com.livable.server.review.controller;

import com.livable.server.review.dto.MyReviewResponse;
import com.livable.server.review.service.MyReviewService;
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

@WebMvcTest(MyReviewController.class)
class MyReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyReviewService myReviewService;

    @Nested
    @DisplayName("나의 레스토랑 리뷰 컨트롤러 단위 테스트")
    class MyRestaurantReview {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/reviews/restaurant/1/members";

            MyReviewResponse.DetailDTO mockResponse
                    = MyReviewResponse.DetailDTO.builder().build();

            Mockito.when(myReviewService.getMyRestaurantReview(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockResponse);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
        }
    }

    @Nested
    @DisplayName("나의 구내식당 리뷰 컨트롤러 단위 테스트")
    class MyCafeteriaReview {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/reviews/cafeteria/1/members";

            MyReviewResponse.DetailDTO mockResponse
                    = MyReviewResponse.DetailDTO.builder().build();

            Mockito.when(myReviewService.getMyCafeteriaReview(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockResponse);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
        }
    }

    @Nested
    @DisplayName("나의 도시락 리뷰 컨트롤러 단위 테스트")
    class MyLunchBoxReview {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/reviews/lunchbox/1/members";

            MyReviewResponse.DetailDTO mockResponse
                    = MyReviewResponse.DetailDTO.builder().build();

            Mockito.when(myReviewService.getMyLunchBoxReview(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
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