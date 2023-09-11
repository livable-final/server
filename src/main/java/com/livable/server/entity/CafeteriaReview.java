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
}
