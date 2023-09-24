package com.livable.server.point.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointResponse {

    @Getter
    @AllArgsConstructor
    public static class ReviewCountDTO {

        private Long count;
    }
}
