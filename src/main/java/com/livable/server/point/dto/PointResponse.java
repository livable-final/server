package com.livable.server.point.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointResponse {

    @Getter
    @AllArgsConstructor
    public static class ReviewCountDTO {

        private Long count;
    }

    @Getter
    @AllArgsConstructor
    public static class CountAndDateDTO {

        private Long count;
        private LocalDateTime mostRecentCreatedDate;
    }
}
