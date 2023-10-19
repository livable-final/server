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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Evaluation taste;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;
}
