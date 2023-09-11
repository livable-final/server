package com.livable.server.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity
@ToString(callSuper = true)
public class CafeteriaReview extends Review {

    private Evaluation taste;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @JoinColumn(nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Building building;
//
//    @JoinColumn(nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member member;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Evaluation taste;
//
//    @Column(nullable = false)
//    private String description;

}
