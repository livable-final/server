package com.livable.server.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity
public class RestaurantReview extends Review {

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Evaluation taste;

    @Column(nullable = false)
    private Evaluation speed;

    @Column(nullable = false)
    private Evaluation amount;

    @Column(nullable = false)
    private Evaluation service;
}
