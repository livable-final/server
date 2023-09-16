package com.livable.server.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation taste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation speed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation service;
}
