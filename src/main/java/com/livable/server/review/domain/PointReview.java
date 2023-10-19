package com.livable.server.review.domain;

import com.livable.server.entity.PointCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointReview {

    RESTAURANT_POINT(PointCode.PA00, 10),
    CAFETERIA_POINT(PointCode.PA01, 10),
    LUNCHBOX_POINT(PointCode.PA02, 10);

    private final PointCode pointCode;
    private final Integer amount;

}
