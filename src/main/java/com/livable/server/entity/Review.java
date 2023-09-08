package com.livable.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation taste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation speed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation service;

}
