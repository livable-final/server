package com.livable.server.review.controller;

import com.livable.server.review.dto.MyReviewResponse;
import com.livable.server.review.dto.RestaurantReviewResponse;
import com.livable.server.review.service.RestaurantReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = RestaurantReviewController.class)
class RestaurantReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantReviewService restaurantReviewService;

    @Nested
    @DisplayName("레스토랑 리뷰 리스트 컨트롤러 단위 테스트")
    class list {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/reviews/buildings/1";

            List<RestaurantReviewResponse.ListDTO> mockList = List.of(
                    RestaurantReviewResponse.ListDTO.builder().reviewId(1L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(2L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(3L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(4L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(5L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(6L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(7L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(8L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(9L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(10L).build()
            );
            Pageable pageable = PageRequest.of(0, 10);
            Page<RestaurantReviewResponse.ListDTO> mockPage = new PageImpl<>(mockList, pageable, 1);

            Mockito.when(restaurantReviewService
                    .getAllList(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(mockPage);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(10));
        }
    }

    @Nested
    @DisplayName("특정 메뉴에 대한 레스토랑 리뷰 리스트 컨트롤러 단위 테스트")
    class listForMenu {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/reviews/menus/1";

            List<RestaurantReviewResponse.ListForMenuDTO> mockList = List.of(
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(1L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(2L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(3L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(4L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(6L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(5L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(7L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(8L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(9L).build(),
                    RestaurantReviewResponse.ListForMenuDTO.builder().reviewId(10L).build()
            );
            Pageable pageable = PageRequest.of(0, 10);
            Page<RestaurantReviewResponse.ListForMenuDTO> mockPage = new PageImpl<>(mockList, pageable, 1);

            Mockito.when(restaurantReviewService
                            .getAllListForMenu(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(mockPage);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(10));
        }
    }

    @Nested
    @DisplayName("레스토랑 리뷰 상세 정보 컨트롤러 단위 테스트")
    class Detail {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            String uri = "/api/reviews/1";

            RestaurantReviewResponse.DetailDTO detailDTO
                    = RestaurantReviewResponse.DetailDTO.builder().build();

            Mockito.when(restaurantReviewService.getDetail(ArgumentMatchers.anyLong()))
                    .thenReturn(detailDTO);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
        }
    }
}