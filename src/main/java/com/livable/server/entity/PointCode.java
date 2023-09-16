package com.livable.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointCode {

    PA00("외부 식당 오점완 리뷰 작성을 통해 포인트 획득"),
    PA01("구내 식당 오점완 리뷰 작성을 통해 포인트 획득"),
    PA02("도시락 오점완 리뷰 작성을 통해 포인트 획득"),
    PA03("오점완 7일차 달성 보상"),
    PA04("오점완 14일차 달성 보상"),
    PA05("오점완 21일차 달성 보상"),
    PM00("제휴 카페 메뉴 할인에 대한 포인트 사용");

    private final String description;
}
