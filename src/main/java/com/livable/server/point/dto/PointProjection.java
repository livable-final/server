package com.livable.server.point.dto;

import com.livable.server.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointProjection {

    @Getter
    @AllArgsConstructor
    public static class CountAndDateDTO {

        private Long count;
        private LocalDateTime mostRecentCreatedDate;
    }

    @Getter
    @AllArgsConstructor
    public static class ReviewAndDateDTO {

        private Review review;
        private LocalDateTime createdAt;
    }
}
