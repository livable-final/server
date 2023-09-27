package com.livable.server.entity;

import com.livable.server.restaurant.dto.RestaurantByMenuProjection;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(
    name ="RestaurantsByMenuMapping",
    classes = @ConstructorResult(
        targetClass = RestaurantByMenuProjection.class,
        columns = {
            @ColumnResult(name = "restaurantId", type = Long.class),
            @ColumnResult(name = "restaurantName", type = String.class),
            @ColumnResult(name = "restaurantThumbnailUrl", type = String.class),
            @ColumnResult(name = "address", type = String.class),
            @ColumnResult(name = "inBuilding", type = Boolean.class),
            @ColumnResult(name = "distance", type = Integer.class),
            @ColumnResult(name = "review", type = String.class),
            @ColumnResult(name = "tastePercentage", type = Integer.class),
        }
    )
)

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantCategory restaurantCategory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String restaurantUrl;

    @Column(nullable = false)
    private String thumbnailImageUrl;

    @Column(nullable = false)
    private String representativeCategory;
}
