package com.livable.server.entity;

import com.livable.server.review.dto.RestaurantReviewProjection;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "RestaurantReviewListMapping",
        classes = @ConstructorResult(
                targetClass = RestaurantReviewProjection.class,
                columns = {
                        @ColumnResult(name = "memberName", type = String.class),
                        @ColumnResult(name = "restaurantId", type = Long.class),
                        @ColumnResult(name = "restaurantName", type = String.class),
                        @ColumnResult(name = "reviewId", type = Long.class),
                        @ColumnResult(name = "reviewCreatedAt", type = LocalDateTime.class),
                        @ColumnResult(name = "reviewDescription", type = String.class),
                        @ColumnResult(name = "reviewTaste", type = String.class),
                        @ColumnResult(name = "reviewAmount", type = String.class),
                        @ColumnResult(name = "reviewService", type = String.class),
                        @ColumnResult(name = "reviewSpeed", type = String.class),
                        @ColumnResult(name = "images", type = String.class),
                }
        )
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(nullable = false)
    private String url;
}
