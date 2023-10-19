package com.livable.server.review.dto;

import com.livable.server.entity.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewRequest {

    @Getter
    @Builder
    @Jacksonized
    public static class LunchBoxCreateDTO {
        // JWT 토큰 오고 => MemberId
        @NotNull(message = "내용을 입력해 주세요.")
        private String description;


        public LunchBoxReview toEntity(Member member, String selectedDishes) {
            return LunchBoxReview.builder()
                    .member(member)
                    .description(description)
                    .selectedDishes(selectedDishes)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CafeteriaCreateDTO {

        private Evaluation taste;

        @NotNull(message = "내용을 입력해 주세요.")
        private String description;

        public CafeteriaReview toEntity(Member member, Building building, String selectedDishes) {
            return CafeteriaReview.builder()
                    .member(member)
                    .taste(taste)
                    .description(description)
                    .selectedDishes(selectedDishes)
                    .building(building)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RestaurantCreateDTO {

        private Long restaurantId;

        @NotNull(message = "내용을 입력해 주세요.")
        private String description;

        private Evaluation taste;
        private Evaluation amount;
        private Evaluation speed;
        private Evaluation service;

        private List<MenuRequest> menus;


        public RestaurantReview toEntity(Member member, Restaurant restaurant, String selectedDishes) {
            return RestaurantReview.builder()
                    .member(member)
                    .restaurant(restaurant)
                    .taste(taste)
                    .amount(amount)
                    .speed(speed)
                    .service(service)
                    .description(description)
                    .selectedDishes(selectedDishes)
                    .build();
        }
    }
}
